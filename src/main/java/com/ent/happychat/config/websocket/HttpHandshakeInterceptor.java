package com.ent.happychat.config.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class HttpHandshakeInterceptor implements HandshakeInterceptor {

    private static final Map<String, String> sessionUserMap = new ConcurrentHashMap<>();

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        // 从ws连接请求路径获取前端传过来的tokenId
        String query = request.getURI().getQuery();
        if (query != null) {
            String[] params = query.split("&");
            for (String param : params) {
                if (param.startsWith("token-id=")) {
                    String token = param.substring("token-id=".length());
                    log.info("拦截器中解析出 token-id: {}", token);
                    attributes.put("token-id", token);
                    break;
                }
            }
        }
        return true;
    }


    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // 握手完成后的逻辑
    }
}
