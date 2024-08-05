package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.ViewsRecord;
import com.ent.happychat.pojo.req.views.ViewsRecordPageReq;

public interface ViewsRecordService extends IService<ViewsRecord> {

    IPage<ViewsRecord> queryPage(ViewsRecordPageReq po);

}
