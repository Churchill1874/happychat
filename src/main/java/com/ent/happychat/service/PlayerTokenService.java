package com.ent.happychat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.PlayerToken;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;

import java.util.List;

public interface PlayerTokenService extends IService<PlayerToken> {

    void updateToken(PlayerToken playerToken);

    void addOrUpdate(Long playerId, String token);

    PlayerToken findByTokenId(String token);

    PlayerToken findByPlayerId(Long playerId);

    List<PlayerToken> loginList();

}
