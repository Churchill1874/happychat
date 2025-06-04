package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.entity.InteractiveStatistics;
import com.ent.happychat.mapper.InteractiveStatisticsMapper;
import com.ent.happychat.pojo.resp.player.PlayerInfoResp;
import com.ent.happychat.service.EhcacheService;
import com.ent.happychat.service.InteractiveStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InteractiveStatisticsServiceImpl extends ServiceImpl<InteractiveStatisticsMapper, InteractiveStatistics> implements InteractiveStatisticsService {

    @Autowired
    private EhcacheService ehcacheService;

    @Override
    public void addCollect(Long playerId) {
        baseMapper.addCollect(playerId);
        ehcacheService.playerInfoCache().remove(playerId.toString());
    }

    @Override
    public void addLikesReceived(Long playerId) {
        baseMapper.addLikesReceived(playerId);
        ehcacheService.playerInfoCache().remove(playerId.toString());
    }

    @Override
    public void addFollowers(Long playerId) {
        baseMapper.addFollowers(playerId);
        ehcacheService.playerInfoCache().remove(playerId.toString());
    }

    @Override
    public InteractiveStatistics findByPlayerId(Long playerId) {
        LambdaQueryWrapper<InteractiveStatistics> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InteractiveStatistics::getPlayerId, playerId);
        InteractiveStatistics interactiveStatistics = getOne(queryWrapper);
        if (interactiveStatistics == null){
            return init(playerId);
        }

        return interactiveStatistics;
    }

    @Override
    public InteractiveStatistics init(Long playerId) {
        InteractiveStatistics interactiveStatistics = new InteractiveStatistics();
        interactiveStatistics.setFollowersCount(0L);
        interactiveStatistics.setLikesReceivedCount(0L);
        interactiveStatistics.setCollectCount(0L);
        interactiveStatistics.setPlayerId(playerId);
        save(interactiveStatistics);

        return interactiveStatistics;
    }

    @Override
    public void assemblyBaseAndStatistics(PlayerInfoResp playerTokenResp) {
        InteractiveStatistics interactiveStatistics = findByPlayerId(playerTokenResp.getId());
        playerTokenResp.setCollectCount(interactiveStatistics.getCollectCount());
        playerTokenResp.setFollowersCount(interactiveStatistics.getFollowersCount());
        playerTokenResp.setLikesReceivedCount(interactiveStatistics.getLikesReceivedCount());
    }


}
