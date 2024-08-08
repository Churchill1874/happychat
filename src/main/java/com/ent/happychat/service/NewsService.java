package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.common.constant.enums.JuHeNewsCategoryEnum;
import com.ent.happychat.common.constant.enums.NewsStatusEnum;
import com.ent.happychat.entity.News;
import com.ent.happychat.pojo.req.likes.LikesClickReq;
import com.ent.happychat.pojo.req.news.NewsPageReq;
import com.ent.happychat.pojo.req.views.ViewsAddReq;
import org.apache.ibatis.annotations.Param;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface NewsService extends IService<News> {

    /**
     * 批量保存新闻
     *
     * @param newsList
     */
    void saveList(List<News> newsList);

    /**
     * 分页查询新闻
     *
     * @param newsPageReq
     */
    IPage<News> queryPage(NewsPageReq newsPageReq);

    /**
     * 根据新闻状态,获取最近几条新闻
     *
     * @param newsStatusEnum
     * @return
     */
    List<News> findByNewsStatus(NewsStatusEnum newsStatusEnum, Integer size);

    /**
     * 删除两个月之前的数据
     */
    void clean2MonthsAgo(LocalDateTime currentTime);


    /**
     * 修改新闻
     *
     * @param news
     */
    void updateNews(News news);


    /**
     * 拉取新闻
     */
    void pullNews(LocalDateTime currentTime, List<JuHeNewsCategoryEnum> newsCategoryEnums, boolean isTask);

    /**
     * 增加评论数量
     * @param id
     */
    void increaseCommentsCount( Long id);

    /**
     * 增加点赞数量
     * @param po
     */
    void increaseLikesCount(LikesClickReq po);

    /**
     * 增加浏览数量
     * @param viewsId
     * @param content
     * @param playerId
     * @param playerName
     */
    void increaseViewsCount(Long viewsId, String content, Long playerId, String playerName);

    /**
     * 查看新闻并插入浏览记录
     * @param viewsId
     * @param playerId
     * @param playerName
     * @return
     */
    News findByIdAndInsertRecord(Long viewsId, Long playerId, String playerName);

    /**
     * 根据id集合获取新闻集合map
     * @param ids
     * @return
     */
    Map<Long, News> mapByIds(List<Long> ids);





}
