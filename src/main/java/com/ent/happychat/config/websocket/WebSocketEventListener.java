package com.ent.happychat.config.websocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebSocketEventListener {

    // 存储当前在线的 sessionId 集合，线程安全
    private static final Set<String> onlineSessions =
            Collections.newSetFromMap(new ConcurrentHashMap<>());

    @EventListener
    public void handleConnect(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        if (sessionId != null) {
            onlineSessions.add(sessionId);
            log.info("WebSocket连接 sessionId={}, 当前真实在线={}", sessionId, onlineSessions.size());
        }
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        if (sessionId != null) {
            onlineSessions.remove(sessionId);
            log.info("WebSocket断开 sessionId={}, 当前真实在线={}", sessionId, onlineSessions.size());
        }
    }

    /**
     * 获取真实 WebSocket 在线连接数
     */
    public static int getRealOnlineCount() {
        return onlineSessions.size();
    }

}
