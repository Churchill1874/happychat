package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.enums.MessageTypeEnum;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.entity.SystemMessage;
import com.ent.happychat.mapper.SystemMessageMapper;
import com.ent.happychat.pojo.req.systemmessage.SystemMessageAddReq;
import com.ent.happychat.pojo.req.systemmessage.SystemMessagePageReq;
import com.ent.happychat.service.PlayerInfoService;
import com.ent.happychat.service.SystemMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SystemMessageServiceImpl extends ServiceImpl<SystemMessageMapper, SystemMessage> implements SystemMessageService {

    @Autowired
    private PlayerInfoService playerInfoService;
    
    @Override
    public IPage<SystemMessage> queryPage(SystemMessagePageReq dto) {
        IPage<SystemMessage> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<SystemMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(dto.getMessageType() != null, SystemMessage::getMessageType, dto.getMessageType())
            .eq(dto.getRecipientId() != null, SystemMessage::getRecipientAccount, dto.getRecipientId())
            .eq(dto.getSenderId() != null, SystemMessage::getSenderAccount, dto.getSenderId())
            .eq(dto.getNewsId() != null, SystemMessage::getNewsId, dto.getNewsId())
            .eq(dto.getSourceType() != null, SystemMessage::getSourceType, dto.getSourceType())
            .orderByDesc(SystemMessage::getCreateTime);
        return page(iPage, queryWrapper);
    }

    @Override
    public void sendSystemMessage(SystemMessageAddReq dto) {
        List<SystemMessage> systemMessageList = new ArrayList<>();

        //获取所有用户对所有用户发送
        List<PlayerInfo> playerInfoList = playerInfoService.queryList(false);
        if (playerInfoList == null){
            throw new DataException("未找到无可发送目标用户");
        }

        for(PlayerInfo playerInfo: playerInfoList){
            SystemMessage systemMessage = new SystemMessage();
            systemMessage.setImagePath(dto.getImagePath());
            systemMessage.setTitle(dto.getTitle());
            systemMessage.setContent(dto.getContent());
            systemMessage.setPopUp(dto.getPopUp());
            systemMessage.setStatus(false);
            systemMessage.setMessageType(MessageTypeEnum.SYSTEM);
            systemMessage.setCreateTime(LocalDateTime.now());
            systemMessage.setCreateName(TokenTools.getAdminName());
            systemMessage.setRecipientAccount(playerInfo.getId());
            systemMessageList.add(systemMessage);
        }
        saveBatch(systemMessageList);
    }

}
