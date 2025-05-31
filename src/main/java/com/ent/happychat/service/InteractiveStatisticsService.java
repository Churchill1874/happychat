package com.ent.happychat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.InteractiveStatistics;
import com.ent.happychat.pojo.resp.player.PlayerInfoResp;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;

public interface InteractiveStatisticsService extends IService<InteractiveStatistics> {

    void addCollect(Long playerId);

    void addLikesReceived(Long playerId);

    void addFollowers(Long playerId);

    InteractiveStatistics findByPlayerId(Long playerId);

    void init(Long playerId);

    void assemblyBaseAndStatistics(PlayerInfoResp playerInfoResp);

}
