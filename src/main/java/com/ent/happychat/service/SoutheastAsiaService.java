package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.News;
import com.ent.happychat.entity.Politics;
import com.ent.happychat.entity.SoutheastAsia;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.pojo.req.southeastasia.SoutheastAsiaPageReq;

import java.util.List;
import java.util.Map;

public interface SoutheastAsiaService extends IService<SoutheastAsia> {

    IPage<SoutheastAsia> queryPage(SoutheastAsiaPageReq req);

    void add(SoutheastAsia req);

    void update(SoutheastAsia req);

    /**
     * 增加评论数量
     * @param id
     */
    void increaseCommentsCount( Long id);

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
     * 查看新闻并插入浏览记录
     * @param viewsId
     * @param playerId
     * @param playerName
     * @param ip
     * @return
     */
    SoutheastAsia findByIdAndInsertRecord(String ip, Long viewsId, Long playerId, String playerName);

    Map<Long, SoutheastAsia> mapByIds(List<Long> ids);


}
