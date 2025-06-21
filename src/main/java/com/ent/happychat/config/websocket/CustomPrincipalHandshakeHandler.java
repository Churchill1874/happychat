package com.ent.happychat.config.websocket;

import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.service.EhcacheService;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.http.server.ServerHttpRequest;

import java.security.Principal;
import java.util.Map;
@Slf4j
public class CustomPrincipalHandshakeHandler extends DefaultHandshakeHandler {
    private final EhcacheService ehcacheService;

    public CustomPrincipalHandshakeHandler(EhcacheService ehcacheService) {
        this.ehcacheService = ehcacheService;
    }

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        // 从 attributes 中取出 Token
        String token = (String) attributes.get("token-id");

        // 如果没有 token，可返回 null 或做其它处理
        if (token == null) {
            return null;
        }

        PlayerTokenResp playerTokenResp =  ehcacheService.playerTokenCache().get(token);
        log.info("握手中提取 token-id={}, playerTokenResp={}", token, playerTokenResp);
        if (playerTokenResp == null){
            return null;
        }

        // 用 token 构造自定义 Principal
        return new StompPrincipal(playerTokenResp.getId().toString());
    }

}
