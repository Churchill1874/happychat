package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.Society;
import com.ent.happychat.entity.Topic;
import com.ent.happychat.pojo.req.society.SocietyPageReq;

import java.util.List;
import java.util.Map;

public interface SocietyService extends IService<Society> {

    IPage<Society> queryPage(SocietyPageReq req);

    void add(Society req);

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
    Society findByIdAndInsertRecord(String ip, Long viewsId, Long playerId, String playerName);

    Map<Long, Society> mapByIds(List<Long> ids);

}
