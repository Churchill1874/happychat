package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.enums.InfoEnum;
import com.ent.happychat.common.constant.enums.MessageTypeEnum;
import com.ent.happychat.common.constant.enums.SystemNoticeEnum;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.Comment;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.entity.SystemMessage;
import com.ent.happychat.mapper.SystemMessageMapper;
import com.ent.happychat.pojo.req.systemmessage.SystemMessageAddReq;
import com.ent.happychat.pojo.req.systemmessage.SystemMessagePageReq;
import com.ent.happychat.service.PlayerInfoService;
import com.ent.happychat.service.SystemMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SystemMessageServiceImpl extends ServiceImpl<SystemMessageMapper, SystemMessage> implements SystemMessageService {

    @Autowired
    private PlayerInfoService playerInfoService;

    //stomp 发送消息的工具类
    private final SimpMessagingTemplate messagingTemplate;

    public SystemMessageServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public IPage<SystemMessage> queryPage(SystemMessagePageReq dto) {
        //异步清理相关类型系统消息未读状态
        readAll(dto.getRecipientId(), dto.getMessageType());

        IPage<SystemMessage> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<SystemMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(dto.getMessageType() != null, SystemMessage::getMessageType, dto.getMessageType())
            .eq(dto.getRecipientId() != null, SystemMessage::getRecipientId, dto.getRecipientId())
            .eq(dto.getSenderId() != null, SystemMessage::getSenderId, dto.getSenderId())
            .eq(dto.getNewsId() != null, SystemMessage::getNewsId, dto.getNewsId())
            .eq(dto.getInfoType() != null, SystemMessage::getInfoType, dto.getInfoType())
            .orderByDesc(SystemMessage::getCreateTime);
        return page(iPage, queryWrapper);
    }

    @Override
    public void sendSystemMessage(SystemMessageAddReq dto) {
        List<SystemMessage> systemMessageList = new ArrayList<>();

        //获取所有用户对所有用户发送
        List<PlayerInfo> playerInfoList = playerInfoService.queryList(false);
        if (playerInfoList == null) {
            throw new DataException("未找到无可发送目标用户");
        }

        for (PlayerInfo playerInfo : playerInfoList) {
            SystemMessage systemMessage = new SystemMessage();
            systemMessage.setImagePath(dto.getImagePath());
            systemMessage.setTitle(dto.getTitle());
            systemMessage.setContent(dto.getContent());
            systemMessage.setPopup(dto.getPopUp());
            systemMessage.setStatus(false);
            systemMessage.setMessageType(MessageTypeEnum.SYSTEM);
            systemMessage.setCreateTime(LocalDateTime.now());
            systemMessage.setCreateName(TokenTools.getAdminName());
            systemMessage.setRecipientId(playerInfo.getId());
            systemMessage.setSystemNoticeType(SystemNoticeEnum.ADMIN);
            systemMessage.setUpdateTime(LocalDateTime.now());
            systemMessageList.add(systemMessage);
        }
        saveBatch(systemMessageList);
    }


    @Override
    //解析是否存在回复他人评论的情况 如果有就给被回复人发一个有人评论了他的评论的消息
    public void sendCommentMessage(Comment dto, String newsTitle, String replyComment) {
        if (dto.getTargetPlayerId() != null && dto.getTargetPlayerId().compareTo(dto.getPlayerId()) != 0) {
            SystemMessage systemMessage = new SystemMessage();
            systemMessage.setContent(dto.getContent());
            systemMessage.setStatus(false);
            systemMessage.setMessageType(MessageTypeEnum.COMMENT);
            systemMessage.setRecipientId(dto.getTargetPlayerId());
            systemMessage.setSenderId(dto.getPlayerId());
            systemMessage.setNewsId(dto.getNewsId());
            systemMessage.setInfoType(dto.getInfoType());
            //systemMessage.setImagePath();
            systemMessage.setPopup(false);
            systemMessage.setCommentId(dto.getId());
            systemMessage.setCreateTime(LocalDateTime.now());
            systemMessage.setCreateName(dto.getCreateName());
            systemMessage.setTitle(newsTitle);
            systemMessage.setComment(replyComment);
            systemMessage.setUpdateTime(LocalDateTime.now());
            save(systemMessage);
            messagingTemplate.convertAndSendToUser(systemMessage.getRecipientId() + "", "/queue/commentMessage", systemMessage);
        }
    }

    public void sendInteractiveMessage(Long senderId, Long recipientId, String title, String content, SystemNoticeEnum systemNoticeEnum) {
        QueryWrapper<SystemMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(SystemMessage::getRecipientId, recipientId)
            .eq(SystemMessage::getSenderId, senderId)
            .eq(SystemMessage::getSystemNoticeType,systemNoticeEnum);
        SystemMessage systemMessage = getOne(queryWrapper);

        if (systemMessage != null){
            systemMessage.setUpdateTime(LocalDateTime.now());
            updateById(systemMessage);
        } else {
            systemMessage = new SystemMessage();
            systemMessage.setCreateTime(LocalDateTime.now());
            systemMessage.setCreateName("系统");
            systemMessage.setMessageType(MessageTypeEnum.SYSTEM);
            systemMessage.setPopup(false);
            systemMessage.setStatus(false);
            systemMessage.setSenderId(senderId);
            systemMessage.setRecipientId(recipientId);
            systemMessage.setTitle(title);
            systemMessage.setContent(content);
            systemMessage.setSystemNoticeType(systemNoticeEnum);
            systemMessage.setUpdateTime(LocalDateTime.now());
            save(systemMessage);
            messagingTemplate.convertAndSendToUser(recipientId + "", "/queue/systemMessage", systemMessage);
        }
    }

    @Async
    @Override
    public void readAll(Long playerId, MessageTypeEnum type) {
        UpdateWrapper<SystemMessage> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
            .set(SystemMessage::getStatus, true)
            .eq(SystemMessage::getMessageType, type)
            .eq(SystemMessage::getRecipientId, playerId)
            .eq(SystemMessage::getStatus, false);
        update(updateWrapper);
    }

    @Override
    public int unreadSystemMessage(Long playerId) {
        QueryWrapper<SystemMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper
            .lambda()
            .eq(SystemMessage::getRecipientId, playerId)
            .eq(SystemMessage::getMessageType, MessageTypeEnum.SYSTEM)
            .eq(SystemMessage::getStatus, false);
        return count(queryWrapper);
    }

}
