package com.ent.happychat.controller.websocket;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ent.happychat.entity.ChatRoom;
import com.ent.happychat.entity.PrivateChat;
import com.ent.happychat.pojo.req.chatroom.ChatRoomSendReq;
import com.ent.happychat.pojo.req.privatechat.PrivateChatSendReq;
import com.ent.happychat.service.ChatRoomService;
import com.ent.happychat.service.PrivateChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Slf4j
@Controller
public class WebSocketController {

    @Autowired
    private PrivateChatService privateChatService;
    @Autowired
    private ChatRoomService chatRoomService;


    //广播发送功能 适用于系统公告 大厅消息等
    @MessageMapping("/system/notice") // 接收前端发送的消息（"/app/chat"）
    @SendTo("/topic/messages") // 广播到 "/topic/messages"
    public String handleChatMessage(String message) {
        log.info("收到消息: {}", message);
        JSONObject jsonObject = JSONUtil.parseObj(message);
        log.info("收到解析后的数据:{}", JSONUtil.toJsonStr(jsonObject));
        return "服务器返回: " + message;
    }

    //聊提室聊天发送功能
    @MessageMapping("/chat/room")
    public void sendRoomMessage(@Payload ChatRoomSendReq req) {
        chatRoomService.send(BeanUtil.toBean(req, ChatRoom.class));
    }

    //私聊发送功能
    @MessageMapping("/chat/private")
    public void sendPrivateMessage(@Payload PrivateChatSendReq req, Principal principal) {
        if (principal == null) {
            log.warn("WebSocket 发送失败：Principal 为空，消息体：{}", req);
            return;
        }

        req.setSendId(principal.getName());
        log.info("{}-发送给了-{}-消息:{}",principal.getName(),req.getReceiveId(),req.getContent());

        PrivateChat privateChat = BeanUtil.toBean(req, PrivateChat.class);
        privateChatService.add(privateChat);
    }



}

