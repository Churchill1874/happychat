package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.exception.TokenException;
import com.ent.happychat.common.tools.HttpTools;
import com.ent.happychat.entity.PlayerToken;
import com.ent.happychat.mapper.PlayerTokenMapper;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.service.EhcacheService;
import com.ent.happychat.service.PlayerInfoService;
import com.ent.happychat.service.PlayerTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PlayerTokenServiceImpl extends ServiceImpl<PlayerTokenMapper, PlayerToken> implements PlayerTokenService {

    //延长时间
    private final int DAYS = 30;

    @Autowired
    private PlayerInfoService playerInfoService;

    @Override
    public PlayerTokenResp checkAndUpdate(String token) {
        PlayerToken playerToken = findByTokenId(token);
        if (playerToken == null) {
            throw new TokenException();
        }

        //更新磁盘存储的token过期时间 过期时间在ehcache配置里面
        updateToken(playerToken);

        //更新缓存时间
        return playerInfoService.updateLoginToken(token, playerToken.getPlayerId());
    }

    @Override
    public void updateToken(PlayerToken playerToken) {
        playerToken.setExpirationTime(LocalDateTime.now().plusDays(DAYS));
        updateById(playerToken);
    }

    @Override
    public void add(PlayerToken playerToken) {
        playerToken.setLoginTime(LocalDateTime.now());
        playerToken.setExpirationTime(playerToken.getLoginTime().plusDays(DAYS));
        playerToken.setIp(HttpTools.getIp());
        save(playerToken);
    }

    @Override
    public PlayerToken findByTokenId(String token) {
        QueryWrapper<PlayerToken> queryWrapper = new QueryWrapper<>();
        queryWrapper
            .lambda()
            .eq(PlayerToken::getTokenId, token);
        return getOne(queryWrapper);
    }


}
