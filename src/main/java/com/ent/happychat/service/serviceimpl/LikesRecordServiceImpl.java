package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.enums.LikesEnum;
import com.ent.happychat.entity.LikesRecord;
import com.ent.happychat.mapper.LikesRecordMapper;
import com.ent.happychat.pojo.req.likes.LikesRecordPageReq;
import com.ent.happychat.service.LikesRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class LikesRecordServiceImpl extends ServiceImpl<LikesRecordMapper, LikesRecord> implements LikesRecordService {

    @Override
    public IPage<LikesRecord> queryPage(LikesRecordPageReq po) {
        IPage<LikesRecord> iPage = new Page<>(po.getPageNum(), po.getPageSize());
        QueryWrapper<LikesRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(po.getLikesId() != null, LikesRecord::getLikesId, po.getLikesId())
                .eq(po.getLikesType() != null, LikesRecord::getLikesType, po.getLikesType())
                .eq(po.getPlayerId() != null, LikesRecord::getPlayerId, po.getPlayerId())
                .eq(po.getContent() != null, LikesRecord::getContent, po.getContent())
                .orderByDesc(LikesRecord::getCreateTime);
        return page(iPage, queryWrapper);
    }

    @Override
    public void increaseLikesCount(Long playerId, String playerName, Long likesId, String content) {
        LikesRecord likesRecord = new LikesRecord();
        likesRecord.setPlayerId(playerId);
        likesRecord.setLikesId(likesId);
        likesRecord.setLikesType(LikesEnum.NEWS);
        likesRecord.setContent(content);
        likesRecord.setCreateTime(LocalDateTime.now());
        likesRecord.setCreateName(playerName);
        save(likesRecord);
    }

}
