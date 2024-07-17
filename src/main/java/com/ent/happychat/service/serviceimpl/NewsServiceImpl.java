package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.CacheKeyConstant;
import com.ent.happychat.common.constant.enums.JuHeNewsCategoryEnum;
import com.ent.happychat.common.constant.enums.NewsCategoryEnum;
import com.ent.happychat.common.constant.enums.NewsStatusEnum;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.common.tools.api.NewsTools;
import com.ent.happychat.entity.News;
import com.ent.happychat.mapper.NewsMapper;
import com.ent.happychat.pojo.req.news.NewsPage;
import com.ent.happychat.pojo.resp.news.HomeNews;
import com.ent.happychat.service.EhcacheService;
import com.ent.happychat.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.ehcache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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
        boolean createTimeSort = !newsPage.getLikesSort() && !newsPage.getViewSort() && !newsPage.getCommentsSort();
        if (!createTimeSort) {
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
    public void pullNews(LocalDateTime currentTime, List<JuHeNewsCategoryEnum> newsCategoryEnums, boolean isTask) {
        //请求新闻定时任务
        int hour = currentTime.getHour();
        int minutes = currentTime.getMinute();
        List<News> newsList = null;

        //每天12点检测一次 只保留2个月的新闻数据 超过的删除
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
    private List<News> pullNewsData(List<JuHeNewsCategoryEnum> newsCategoryEnums) {
        List<News> newsList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(newsCategoryEnums)) {
            if (newsCategoryEnums.contains(JuHeNewsCategoryEnum.HEADLINES)) {
                //头条
                List<News> headlinesNews = NewsTools.getNewsData(JuHeNewsCategoryEnum.HEADLINES, 10);
                newsList.addAll(headlinesNews);
            }
            if (newsCategoryEnums.contains(JuHeNewsCategoryEnum.NEWS)) {
                //新闻
                List<News> news = NewsTools.getNewsData(JuHeNewsCategoryEnum.NEWS, 10);
                newsList.addAll(news);
            }
            if (newsCategoryEnums.contains(JuHeNewsCategoryEnum.SPORTS)) {
                //体育
                List<News> sportsNews = NewsTools.getNewsData(JuHeNewsCategoryEnum.SPORTS, 10);
                newsList.addAll(sportsNews);
            }
            if (newsCategoryEnums.contains(JuHeNewsCategoryEnum.MILITARY_AFFAIRS)) {
                //军事
                List<News> militaryAffairsNews = NewsTools.getNewsData(JuHeNewsCategoryEnum.MILITARY_AFFAIRS, 10);
                newsList.addAll(militaryAffairsNews);
            }
            if (newsCategoryEnums.contains(JuHeNewsCategoryEnum.SCIENCE)) {
                //科技
                List<News> scienceNews = NewsTools.getNewsData(JuHeNewsCategoryEnum.SCIENCE, 10);
                newsList.addAll(scienceNews);
            }
            if (newsCategoryEnums.contains(JuHeNewsCategoryEnum.ENTERTAINMENT)) {
                //娱乐
                List<News> entertainmentNews = NewsTools.getNewsData(JuHeNewsCategoryEnum.ENTERTAINMENT, 10);
                newsList.addAll(entertainmentNews);
            }
/*            if (newsCategoryEnums.contains(JuHeNewsCategoryEnum.WOMAN)) {
                //人情
                List<News> womanNews = NewsTools.getNewsData(JuHeNewsCategoryEnum.WOMAN, 7);
                newsList.addAll(womanNews);
            }*/
        }
        return newsList;
    }
}
