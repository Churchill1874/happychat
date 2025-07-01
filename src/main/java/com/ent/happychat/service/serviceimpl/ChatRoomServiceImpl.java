package com.ent.happychat.service.serviceimpl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.entity.ChatRoom;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.mapper.ChatRoomMapper;
import com.ent.happychat.pojo.req.PageAndIdReq;
import com.ent.happychat.pojo.resp.chatroom.ChatRoomResp;
import com.ent.happychat.service.ChatRoomService;
import com.ent.happychat.service.PlayerInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChatRoomServiceImpl extends ServiceImpl<ChatRoomMapper, ChatRoom> implements ChatRoomService {

    //stomp 发送消息的工具类
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    private PlayerInfoService playerInfoService;

    public ChatRoomServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public IPage<ChatRoom> queryPage(PageAndIdReq dto) {
        IPage<ChatRoom> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<ChatRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper
            .lambda()
            .eq(ChatRoom::getRoomNumber, dto.getId())
            .orderByAsc(ChatRoom::getCreateTime);
        return page(iPage, queryWrapper);
    }

    @Override
    public void send(ChatRoom dto) {
        dto.setCreateTime(LocalDateTime.now());
        dto.setIsReply(dto.getTargetPlayerId() != null && StringUtils.isNotBlank(dto.getReplyContent()));
        save(dto);

        ChatRoomResp chatRoomResp = BeanUtil.toBean(dto, ChatRoomResp.class);
        PlayerInfo playerInfo = playerInfoService.getById(chatRoomResp.getPlayerId());
        chatRoomResp.setName(playerInfo.getName());
        chatRoomResp.setLevel(playerInfo.getLevel());
        chatRoomResp.setAvatarPath(playerInfo.getAvatarPath());
        messagingTemplate.convertAndSend("/topic/room/" + dto.getRoomNumber(), chatRoomResp);
    }

}
