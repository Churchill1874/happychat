package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.Exposure;
import com.ent.happychat.entity.SoutheastAsia;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.pojo.req.exposure.ExposurePage;

public interface ExposureService extends IService<Exposure> {

    IPage<Exposure> queryPage(ExposurePage dto);

    void add(Exposure dto);

    void update(Exposure dto);

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
    Exposure findByIdAndInsertRecord(String ip, Long viewsId, Long playerId, String playerName);
}
