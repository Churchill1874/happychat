package com.ent.happychat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.PlayerToken;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;

public interface PlayerTokenService extends IService<PlayerToken> {

    PlayerTokenResp checkAndUpdate(String token);

    void updateToken(PlayerToken playerToken);

    void add(PlayerToken playerToken);

    PlayerToken findByTokenId(String token);

}
