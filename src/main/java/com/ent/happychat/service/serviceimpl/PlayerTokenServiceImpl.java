package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.tools.HttpTools;
import com.ent.happychat.entity.PlayerToken;
import com.ent.happychat.mapper.PlayerTokenMapper;
import com.ent.happychat.service.PlayerTokenService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PlayerTokenServiceImpl extends ServiceImpl<PlayerTokenMapper, PlayerToken> implements PlayerTokenService {

    //延长时间
    private final int DAYS = 30;


    @Override
    public void updateToken(PlayerToken playerToken) {
        playerToken.setIp(HttpTools.getIp());
        playerToken.setCity(HttpTools.getAddress());
        playerToken.setExpirationTime(LocalDateTime.now().plusDays(DAYS));
        updateById(playerToken);
    }

    @Override
    public void addOrUpdate(Long playerId, String token) {
        PlayerToken playerToken = findByPlayerId(playerId);

        if (playerToken == null) {
            playerToken = new PlayerToken();
            playerToken.setPlayerId(playerId);
            playerToken.setLoginTime(LocalDateTime.now());
            playerToken.setExpirationTime(playerToken.getLoginTime().plusDays(DAYS));
            playerToken.setIp(HttpTools.getIp());
            playerToken.setTokenId(token);
            playerToken.setCity(HttpTools.getAddress());
            save(playerToken);
        } else {
            updateToken(playerToken);
        }


    }

    @Override
    public PlayerToken findByTokenId(String token) {
        QueryWrapper<PlayerToken> queryWrapper = new QueryWrapper<>();
        queryWrapper
            .lambda()
            .eq(PlayerToken::getTokenId, token);
        return getOne(queryWrapper);
    }

    @Override
    public PlayerToken findByPlayerId(Long playerId) {
        QueryWrapper<PlayerToken> queryWrapper = new QueryWrapper<>();
        queryWrapper
            .lambda()
            .eq(PlayerToken::getPlayerId, playerId);
        return getOne(queryWrapper);
    }


}
