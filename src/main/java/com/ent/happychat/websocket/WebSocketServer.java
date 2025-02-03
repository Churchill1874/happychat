package com.ent.happychat.websocket;

import lombok.extern.slf4j.Slf4j;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerEndpoint("/ws/{username}")
public class WebSocketServer {

    private static final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

    private String username;

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        this.username = username;
        sessions.put(username, session);
        log.info(username + " 连接成功");
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        log.info("收到 " + username + " 发送的消息：" + message);
        for (Session s : sessions.values()) {
            s.getBasicRemote().sendText(username + "：" + message);
        }
    }

    @OnClose
    public void onClose() {
        sessions.remove(username);
        log.info(username + " 断开连接");
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket 发生错误：" + error.getMessage());
    }


}
