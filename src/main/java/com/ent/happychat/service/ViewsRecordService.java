package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.common.constant.enums.ViewsEnum;
import com.ent.happychat.entity.ViewsRecord;
import com.ent.happychat.pojo.req.views.ViewsAddReq;
import com.ent.happychat.pojo.req.views.ViewsRecordPageReq;

public interface ViewsRecordService extends IService<ViewsRecord> {

    IPage<ViewsRecord> queryPage(ViewsRecordPageReq po);

    /**
     * 添加浏览记录
     * @param viewsId
     * @param content
     * @param playerId
     * @param playerName
     * @param ip
     */
    void addViewsRecord(String ip, Long viewsId, String content, Long playerId, String playerName, ViewsEnum viewType);


}
