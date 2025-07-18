package com.ent.happychat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.InteractiveStatistics;
import com.ent.happychat.pojo.resp.player.PlayerInfoResp;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;

public interface InteractiveStatisticsService extends IService<InteractiveStatistics> {

    void addCollect(Long playerId);

    void subCollect(Long playerId);

    void addLikesReceived(Long playerId);

    void subLikesReceived(Long playerId);

    void addFollowers(Long playerId);

    void subFollowers(Long playerId);

    InteractiveStatistics findByPlayerId(Long playerId);

    InteractiveStatistics init(Long playerId);

    void assemblyBaseAndStatistics(PlayerInfoResp playerInfoResp);

}
