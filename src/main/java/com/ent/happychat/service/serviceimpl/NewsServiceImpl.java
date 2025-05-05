package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.CacheKeyConstant;
import com.ent.happychat.common.constant.enums.JuHeNewsCategoryEnum;
import com.ent.happychat.common.constant.enums.LikesEnum;
import com.ent.happychat.common.constant.enums.NewsStatusEnum;
import com.ent.happychat.common.constant.enums.ViewsEnum;
import com.ent.happychat.common.exception.AuthException;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.exception.TokenException;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.common.tools.api.NewsTools;
import com.ent.happychat.entity.LikesRecord;
import com.ent.happychat.entity.News;
import com.ent.happychat.entity.ViewsRecord;
import com.ent.happychat.mapper.NewsMapper;
import com.ent.happychat.pojo.req.likes.LikesClickReq;
import com.ent.happychat.pojo.req.news.NewsPageReq;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.service.EhcacheService;
import com.ent.happychat.service.LikesRecordService;
import com.ent.happychat.service.NewsService;
import com.ent.happychat.service.ViewsRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

    @Autowired
    private EhcacheService ehcacheService;
    @Autowired
    private LikesRecordService likesRecordService;
    @Autowired
    private ViewsRecordService viewsRecordService;

    @Override
    public void saveList(List<News> newsList) {
        this.saveBatch(newsList);
    }

    @Override
    public IPage<News> queryPage(NewsPageReq newsPageReq) {
        IPage<News> iPage = new Page<>(newsPageReq.getPageNum(), newsPageReq.getPageSize());

        QueryWrapper<News> queryNews = new QueryWrapper<>();
        queryNews.lambda()
                .eq(newsPageReq.getCategoryEnum() != null, News::getCategory, newsPageReq.getCategoryEnum())
                .eq(newsPageReq.getNewsStatus() != null, News::getNewsStatus, newsPageReq.getNewsStatus())
                .like(StringUtils.isNotBlank(newsPageReq.getTitle()), News::getTitle, newsPageReq.getTitle())
                .orderByDesc(News::getCreateTime);

        //如果没有要求按照 差评 点赞 浏览 评论数量 要求筛选
        boolean createTimeSort = !newsPageReq.getLikesSort() && !newsPageReq.getViewSort() && !newsPageReq.getCommentsSort();
        if (!createTimeSort) {
            if (newsPageReq.getLikesSort()) {
                queryNews.lambda().orderByDesc(News::getLikesCount);
            }
            if (newsPageReq.getViewSort()) {
                queryNews.lambda().orderByDesc(News::getViewCount);
            }
            if (newsPageReq.getCommentsSort()) {
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
            baseMapper.insertBatchIgnore(newsList);

            //清理首页的新闻列表缓存
            ehcacheService.homeNewsCache().remove(CacheKeyConstant.HOME_NEWS_KEY);
        }
    }

    @Override
    public void increaseCommentsCount(Long id) {
        baseMapper.increaseCommentsCount(id);
    }


    @Override
    public boolean increaseLikesCount(Long id) {
        News news = getById(id);
        if (news == null){
            throw new DataException("内容不存在或已删除");
        }
        //插入点赞记录
        PlayerTokenResp playerTokenResp = TokenTools.getPlayerToken(true);
        QueryWrapper<LikesRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(LikesRecord::getPlayerId, playerTokenResp.getId())
                .eq(LikesRecord::getLikesId, id)
                .eq(LikesRecord::getLikesType, LikesEnum.NEWS);
        int count = likesRecordService.count(queryWrapper);

        if(count == 0){
            likesRecordService.increaseLikesCount(
                    playerTokenResp.getId(),
                    playerTokenResp.getName(),
                    id,
                    news.getTitle(),
                    LikesEnum.NEWS,
                    null
            );

            baseMapper.increaseLikesCount(id);
            return true;
        } else {
            return false;
        }

    }

    @Override
    @Async
    public void increaseViewsCount(String ip, Long viewsId, String content, Long playerId, String playerName) {
        viewsRecordService.addViewsRecord(ip, viewsId, content, playerId, playerName, ViewsEnum.NEWS);
        baseMapper.increaseViewsCount(viewsId);
    }

    @Override
    public News findByIdAndInsertRecord(String ip, Long viewsId, Long playerId, String playerName) {
        if (playerId == null){
            throw new TokenException();
        }
        News news = getById(viewsId);
        increaseViewsCount(ip, viewsId, news.getTitle(), playerId, playerName);
        return news;
    }

    @Override
    public Map<Long, News> mapByIds(List<Long> ids) {
        Map<Long, News> map = new HashMap<>();

        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(News::getId, ids);
        List<News> list = list(queryWrapper);

        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(news -> {
                map.put(news.getId(), news);
            });
        }
        return map;
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

        Collections.shuffle(newsList);
        return newsList;
    }
}
