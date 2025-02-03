package com.ent.happychat.controller.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class WebSocketController {

    @MessageMapping("/chat") // 接收前端发送的消息（"/app/chat"）
    @SendTo("/topic/messages") // 广播到 "/topic/messages"
    public String handleChatMessage(String message) {
        log.info("收到消息: {}", message);
        return "服务器返回: " + message;
    }

}

