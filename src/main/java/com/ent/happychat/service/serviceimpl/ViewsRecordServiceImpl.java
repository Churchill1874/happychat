package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.enums.ViewsEnum;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.ViewsRecord;
import com.ent.happychat.mapper.ViewsRecordMapper;
import com.ent.happychat.pojo.req.views.ViewsAddReq;
import com.ent.happychat.pojo.req.views.ViewsRecordPageReq;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.service.ViewsRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class ViewsRecordServiceImpl extends ServiceImpl<ViewsRecordMapper, ViewsRecord> implements ViewsRecordService {

    @Override
    public IPage<ViewsRecord> queryPage(ViewsRecordPageReq po) {
        IPage<ViewsRecord> iPage = new Page<>(po.getPageNum(), po.getPageSize());
        QueryWrapper<ViewsRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(po.getPlayerId() != null, ViewsRecord::getPlayerId, po.getPlayerId())
                .eq(po.getViewsId() != null, ViewsRecord::getViewsId, po.getViewsId())
                .eq(po.getViewsType() != null, ViewsRecord::getViewsType, po.getViewsType())
                .eq(po.getContent() != null, ViewsRecord::getContent, po.getContent())
                .orderByDesc(ViewsRecord::getCreateTime);
        return page(iPage, queryWrapper);
    }

    @Override
    public void addViewsRecord(ViewsAddReq po, Long playerId, String playerName) {
        // 插入浏览记录
        PlayerTokenResp playerTokenResp = TokenTools.getPlayerToken(true);
        ViewsRecord viewsRecord = new ViewsRecord();
        viewsRecord.setPlayerId(playerId);
        viewsRecord.setViewsId(po.getViewsId());
        viewsRecord.setViewsType(ViewsEnum.NEWS);
        viewsRecord.setContent(po.getContent());
        viewsRecord.setCreateTime(LocalDateTime.now());
        viewsRecord.setCreateName(playerName);
        save(viewsRecord);
    }

}
