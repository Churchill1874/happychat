package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.entity.ViewsRecord;
import com.ent.happychat.mapper.ViewsRecordMapper;
import com.ent.happychat.pojo.req.views.ViewsRecordPageReq;
import com.ent.happychat.service.ViewsRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ViewsRecordServiceImpl extends ServiceImpl<ViewsRecordMapper, ViewsRecord> implements ViewsRecordService {


    @Override
    public IPage<ViewsRecord> queryPage(ViewsRecordPageReq po) {
        return null;
    }
}
