package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.common.constant.enums.JuHeNewsCategoryEnum;
import com.ent.happychat.common.constant.enums.NewsStatusEnum;
import com.ent.happychat.entity.News;
import com.ent.happychat.pojo.req.news.NewsPageReq;
import org.apache.ibatis.annotations.Param;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;
import java.util.List;

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
    void increaseCommentsCount(@Param("id") Long id);

    /**
     * 增加点赞数量
     * @param id
     */
    void increaseLikesCount(@Param("id") Long id);

    /**
     * 查看新闻并插入浏览记录
     * @param id
     * @return
     */
    News findByIdAndInsertRecord(Long id);


}
