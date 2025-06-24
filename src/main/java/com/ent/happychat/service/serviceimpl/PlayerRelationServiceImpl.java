package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.entity.PlayerRelation;
import com.ent.happychat.mapper.PlayerRelationMapper;
import com.ent.happychat.pojo.req.playerrelation.PlayerRelationPageReq;
import com.ent.happychat.service.InteractiveStatisticsService;
import com.ent.happychat.service.PlayerRelationService;
import com.ent.happychat.service.SystemMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PlayerRelationServiceImpl extends ServiceImpl<PlayerRelationMapper, PlayerRelation> implements PlayerRelationService {

    @Autowired
    private SystemMessageService systemMessageService;
    @Autowired
    private InteractiveStatisticsService interactiveStatisticsService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(PlayerRelation playerRelation, String content) {
        playerRelation.setCreateTime(LocalDateTime.now());
        save(playerRelation);

        //a关注了b,a就要多一个关注
        //b就要多一个粉丝
        interactiveStatisticsService.addCollect(playerRelation.getPlayerId());
        interactiveStatisticsService.addFollowers(playerRelation.getTargetPlayerId());

        //发送系统通知 提示被关注人有人关注了他
        systemMessageService.sendInteractiveMessage(playerRelation.getPlayerId(),
            playerRelation.getTargetPlayerId(),
            "您有新的粉丝关注",
            content
        );

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long playerId, Long targetPlayerId) {
        QueryWrapper<PlayerRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper
            .lambda()
            .eq(PlayerRelation::getPlayerId, playerId)
            .eq(PlayerRelation::getTargetPlayerId, targetPlayerId);
        remove(queryWrapper);

        //a取消关注了b,a就要少一个关注
        //b就要少一个粉丝
        interactiveStatisticsService.subCollect(playerId);
        interactiveStatisticsService.subFollowers(targetPlayerId);
    }

    @Override
    public IPage<PlayerRelation> queryPage(PlayerRelationPageReq dto) {
        IPage<PlayerRelation> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<PlayerRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
            .eq(dto.getPlayerId() != null, PlayerRelation::getPlayerId, dto.getPlayerId())
            .eq(dto.getTargetPlayerId() != null, PlayerRelation::getTargetPlayerId, dto.getTargetPlayerId())
            .orderByDesc(PlayerRelation::getCreateTime);
        return page(iPage, queryWrapper);
    }

    @Override
    public PlayerRelation queryRelation(Long playerId, Long targetPlayerId) {
        QueryWrapper<PlayerRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(PlayerRelation::getPlayerId, playerId)
            .eq(PlayerRelation::getTargetPlayerId, targetPlayerId);
        return getOne(queryWrapper);
    }

    @Override
    public Set<Long> relationSet(Long playerId, List<Long> targetPlayerIdList) {
        Set<Long> set = new HashSet<>();
        //以当前玩家作为目标 查询列表中的玩家id是否被自己关注
        QueryWrapper<PlayerRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(PlayerRelation::getPlayerId, playerId)
            .in(PlayerRelation::getTargetPlayerId, targetPlayerIdList);

        List<PlayerRelation> playerRelationList = list(queryWrapper);
        if (CollectionUtils.isEmpty(playerRelationList)) {
            return set;
        }

        for (PlayerRelation playerRelation : playerRelationList) {
            set.add(playerRelation.getTargetPlayerId());
        }
        return set;
    }

}
