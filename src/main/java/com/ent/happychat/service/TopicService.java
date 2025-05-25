package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.Promotion;
import com.ent.happychat.entity.Topic;
import com.ent.happychat.pojo.req.promotion.PromotionAddReq;
import com.ent.happychat.pojo.req.promotion.PromotionPageReq;
import com.ent.happychat.pojo.req.topic.TopicAddReq;
import com.ent.happychat.pojo.req.topic.TopicPageReq;

public interface TopicService extends IService<Topic> {

    IPage<Topic> queryPage(TopicPageReq req);

    void add(TopicAddReq req);

    /**
     * 增加评论数量
     *
     * @param id
     */
    void increaseCommentsCount(Long id);

    /**
     * 增加浏览数量
     *
     * @param ip
     * @param viewsId
     * @param content
     * @param playerId
     * @param playerName
     */
    void increaseViewsCount(String ip, Long viewsId, String content, Long playerId, String playerName);

    /**
     * 查看新闻并插入浏览记录
     *
     * @param viewsId
     * @param playerId
     * @param playerName
     * @param ip
     * @return
     */
    Topic findByIdAndInsertRecord(String ip, Long viewsId, Long playerId, String playerName);


}
