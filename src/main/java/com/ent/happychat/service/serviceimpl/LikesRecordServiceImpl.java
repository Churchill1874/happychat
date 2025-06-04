package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.enums.InfoEnum;
import com.ent.happychat.common.constant.enums.LikesEnum;
import com.ent.happychat.entity.LikesRecord;
import com.ent.happychat.mapper.LikesRecordMapper;
import com.ent.happychat.pojo.req.likes.LikesRecordPageReq;
import com.ent.happychat.service.LevelProgressService;
import com.ent.happychat.service.LikesRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class LikesRecordServiceImpl extends ServiceImpl<LikesRecordMapper, LikesRecord> implements LikesRecordService {

    @Autowired
    private LevelProgressService levelProgressService;

    @Override
    public IPage<LikesRecord> queryPage(LikesRecordPageReq po) {
        IPage<LikesRecord> iPage = new Page<>(po.getPageNum(), po.getPageSize());
        QueryWrapper<LikesRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(po.getLikesId() != null, LikesRecord::getLikesId, po.getLikesId())
            .eq(po.getLikesType() != null, LikesRecord::getLikesType, po.getLikesType())
            .eq(po.getPlayerId() != null, LikesRecord::getPlayerId, po.getPlayerId())
            .eq(po.getContent() != null, LikesRecord::getContent, po.getContent())
            .eq(po.getTargetPlayerId() != null, LikesRecord::getTargetPlayerId, po.getTargetPlayerId())
            .orderByDesc(LikesRecord::getCreateTime);
        return page(iPage, queryWrapper);
    }


    @Async
    @Override
    public void increaseLikesCount(Long playerId, String playerName, Long likesId, String content, LikesEnum likesType, Long targetPlayerId, InfoEnum infoType) {
        LikesRecord likesRecord = new LikesRecord();
        likesRecord.setPlayerId(playerId);
        likesRecord.setLikesId(likesId);
        likesRecord.setLikesType(likesType);
        likesRecord.setContent(content);
        likesRecord.setCreateTime(LocalDateTime.now());
        likesRecord.setCreateName(playerName);
        likesRecord.setTargetPlayerId(targetPlayerId);
        likesRecord.setInfoType(infoType);
        save(likesRecord);

        //todo 要查询被点赞的玩家 当前 被点赞数量 和 下注正确数量 是否可以升级了
        if (likesType == LikesEnum.COMMENT) {
            levelProgressService.levelProgress(targetPlayerId);
        }

    }


}
