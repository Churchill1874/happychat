package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.News;
import com.ent.happychat.entity.Politics;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import org.apache.ibatis.annotations.Param;

public interface PoliticsService extends IService<Politics> {

    IPage<Politics> queryPage(PageBase dto);

    /**
     * 增加点赞数量
     * @param id
     */
    void increaseLikesCount(Long id, PlayerTokenResp playerTokenResp);

    /**
     * 增加浏览数量
     * @param ip
     * @param viewsId
     * @param content
     * @param playerId
     * @param playerName
     */
    void increaseViewsCount(String ip, Long viewsId, String content, Long playerId, String playerName);

    /**
     * 增加评论数量
     * @param id
     */
    void increaseCommentsCount( Long id);

    /**
     * 查看新闻并插入浏览记录
     * @param viewsId
     * @param playerId
     * @param playerName
     * @param ip
     * @return
     */
    Politics findByIdAndInsertRecord(String ip, Long viewsId, Long playerId, String playerName);

}
