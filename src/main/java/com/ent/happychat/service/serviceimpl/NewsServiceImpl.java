package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.CacheKeyConstant;
import com.ent.happychat.common.constant.enums.NewsCategoryEnum;
import com.ent.happychat.common.constant.enums.NewsStatusEnum;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.common.tools.api.NewsTools;
import com.ent.happychat.entity.News;
import com.ent.happychat.mapper.NewsMapper;
import com.ent.happychat.pojo.req.news.NewsPage;
import com.ent.happychat.service.EhcacheService;
import com.ent.happychat.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

    @Autowired
    private NewsMapper newsMapper;
    @Autowired
    private EhcacheService ehcacheService;

    @Override
    public void saveList(List<News> newsList) {
        this.saveBatch(newsList);
    }

    @Override
    public IPage<News> queryPage(NewsPage newsPage) {
        IPage<News> iPage = new Page<>(newsPage.getPageNum(), newsPage.getPageSize());

        QueryWrapper<News> queryNews = new QueryWrapper<>();
        queryNews.lambda()
                .eq(newsPage.getCategoryEnum() != null, News::getCategory, newsPage.getCategoryEnum())
                .eq(newsPage.getNewsStatus() != null, News::getNewsStatus, newsPage.getNewsStatus())
                .like(StringUtils.isNotBlank(newsPage.getTitle()), News::getTitle, newsPage.getTitle())
                .orderByDesc(News::getCreateTime);

        //如果没有要求按照 差评 点赞 浏览 评论数量 要求筛选
        boolean createTimeSort = !newsPage.getBadSort() && !newsPage.getLikesSort() && !newsPage.getViewSort() && !newsPage.getCommentsSort();
        if (createTimeSort) {
            queryNews.lambda().orderByDesc(News::getCreateTime);
        } else {
            if (newsPage.getBadSort()) {
                queryNews.lambda().orderByDesc(News::getBadCount);
            }
            if (newsPage.getLikesSort()) {
                queryNews.lambda().orderByDesc(News::getLikesCount);
            }
            if (newsPage.getViewSort()) {
                queryNews.lambda().orderByDesc(News::getViewCount);
            }
            if (newsPage.getCommentsSort()) {
                queryNews.lambda().orderByDesc(News::getCommentsCount);
            }
        }

        return page(iPage, queryNews);
    }

    @Override
    public List<News> findByNewsStatus(NewsStatusEnum newsStatusEnum, Integer size) {
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(News::getNewsStatus, newsStatusEnum)
                .orderByDesc(News::getCreateTime)
                .last("LIMIT " + size);

        return this.list(queryWrapper);
    }

    @Override
    public void clean2MonthsAgo(LocalDateTime currentTime) {
        LocalDateTime twoMonthsAgo = currentTime.minus(2, ChronoUnit.MONTHS);

        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().le(News::getCreateTime, twoMonthsAgo);

        remove(queryWrapper);
    }

    @Override
    public void updateNews(News news) {
        News record = getById(news.getId());
        if (record == null) {
            throw new DataException("内容不存在或已删除");
        }
        if (news.getNewsStatus() != null) {
            record.setNewsStatus(news.getNewsStatus());
        }
        if (news.getLikesCount() != null) {
            record.setLikesCount(news.getLikesCount());
        }
        if (news.getBadCount() != null) {
            record.setBadCount(news.getBadCount());
        }
        if (news.getContent() != null) {
            record.setContent(news.getContent());
        }
        if (news.getTitle() != null) {
            record.setTitle(news.getTitle());
        }
        if (news.getViewCount() != null) {
            record.setViewCount(news.getViewCount());
        }
        if (news.getContentImagePath() != null) {
            record.setContentImagePath(news.getContentImagePath());
        }
        record.setUpdateName(TokenTools.getAdminToken(true).getName());
        record.setUpdateTime(LocalDateTime.now());
        updateById(record);
    }

    @Async
    @Override
    public void pullNews(LocalDateTime currentTime, List<NewsCategoryEnum> newsCategoryEnums, boolean isTask) {
        //请求新闻定时任务
        int hour = currentTime.getHour();
        int minutes = currentTime.getMinute();
        List<News> newsList = null;

        //只保留2个月的新闻数据 超过的删除
        if (hour == 0 && minutes == 0) {
            clean2MonthsAgo(currentTime);
        }

        //如果是定时任务
        if (isTask) {
            //6点 到晚上 23点 之间 每小时获取一次 军事,头条,新闻,体育,娱乐新闻
            if (hour >= 6 && minutes == 0) {
                newsList = pullNewsData(newsCategoryEnums);
            }
        } else {//否则
            newsList = pullNewsData(newsCategoryEnums);
        }

        if (CollectionUtils.isNotEmpty(newsList)) {
            log.info("执行新闻定时任务,处理新闻数据{}条", newsList.size());
            newsMapper.insertBatchIgnore(newsList);

            //清理首页的新闻列表缓存
            ehcacheService.homeNewsCache().remove(CacheKeyConstant.HOME_NEWS_KEY);
        }
    }


    //拉取新闻数据
    private List<News> pullNewsData(List<NewsCategoryEnum> newsCategoryEnums) {
        List<News> newsList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(newsCategoryEnums)) {
            if (newsCategoryEnums.contains(NewsCategoryEnum.HEADLINES)) {
                //头条
                List<News> headlinesNews = NewsTools.getNewsData(NewsCategoryEnum.HEADLINES, 10);
                newsList.addAll(headlinesNews);
            }
            if (newsCategoryEnums.contains(NewsCategoryEnum.SPORTS)) {
                //体育
                List<News> sportsNews = NewsTools.getNewsData(NewsCategoryEnum.SPORTS, 10);
                newsList.addAll(sportsNews);
            }
            if (newsCategoryEnums.contains(NewsCategoryEnum.NEWS)) {
                //新闻
                List<News> news = NewsTools.getNewsData(NewsCategoryEnum.NEWS, 10);
                newsList.addAll(news);
            }
            if (newsCategoryEnums.contains(NewsCategoryEnum.MILITARY_AFFAIRS)) {
                //军事
                List<News> militaryAffairsNews = NewsTools.getNewsData(NewsCategoryEnum.MILITARY_AFFAIRS, 7);
                newsList.addAll(militaryAffairsNews);
            }
            if (newsCategoryEnums.contains(NewsCategoryEnum.SCIENCE)) {
                //科技
                List<News> scienceNews = NewsTools.getNewsData(NewsCategoryEnum.SCIENCE, 5);
                newsList.addAll(scienceNews);
            }
            if (newsCategoryEnums.contains(NewsCategoryEnum.ENTERTAINMENT)) {
                //娱乐
                List<News> entertainmentNews = NewsTools.getNewsData(NewsCategoryEnum.ENTERTAINMENT, 5);
                newsList.addAll(entertainmentNews);
            }
            if (newsCategoryEnums.contains(NewsCategoryEnum.WOMAN)) {
                //女性
                List<News> womenNews = NewsTools.getNewsData(NewsCategoryEnum.WOMAN, 3);
                newsList.addAll(womenNews);
            }
        }
        return newsList;
    }
}
