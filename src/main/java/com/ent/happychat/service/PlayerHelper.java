package com.ent.happychat.service;

import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;

public interface PlayerHelper {

    /**
     * 生成token缓存
     * @param playerInfo
     * @return
     */
    PlayerTokenResp createLoginToken(PlayerInfo playerInfo);

    /**
     * 更新token缓存
     * @param token
     * @param playerId
     * @return
     */
    PlayerTokenResp updateLoginToken(String token, Long playerId);


    PlayerTokenResp checkAndUpdate(String token);

}
