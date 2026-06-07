package com.ent.happychat.service.serviceimpl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.entity.PrivateChat;
import com.ent.happychat.mapper.PrivateChatMapper;
import com.ent.happychat.pojo.req.privatechat.PlayerPrivateChatPageReq;
import com.ent.happychat.pojo.resp.player.PlayerInfoResp;
import com.ent.happychat.pojo.resp.privatechat.PrivateChatResp;
import com.ent.happychat.service.PlayerInfoService;
import com.ent.happychat.service.PrivateChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class PrivateChatServiceImpl extends ServiceImpl<PrivateChatMapper, PrivateChat> implements PrivateChatService {


    //stomp 发送消息的工具类
    private final SimpMessagingTemplate messagingTemplate;

    public PrivateChatServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Autowired
    private PlayerInfoService playerInfoService;

    @Override
    public List<PrivateChat> listByAccountId(Long accountId) {
        QueryWrapper<PrivateChat> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(PrivateChat::getSendId, accountId)
            .or()
            .eq(PrivateChat::getReceiveId, accountId)
            .orderByDesc(PrivateChat::getCreateTime);
        return list(queryWrapper);
    }

    @Override
    public IPage<PrivateChat> playerPrivateChatPage(PlayerPrivateChatPageReq po) {
        IPage<PrivateChat> iPage = new Page<>(po.getPageNum(), po.getPageSize());

        QueryWrapper<PrivateChat> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda()
            .select(PrivateChat::getSendId, PrivateChat::getReceiveId, PrivateChat::getContent, PrivateChat::getCreateTime)
            .and(
                query -> query
                    .eq(PrivateChat::getSendId, po.getPlayerAId())
                    .eq(PrivateChat::getReceiveId, po.getPlayerBId())
                    .or()
                    .eq(PrivateChat::getSendId, po.getPlayerBId())
                    .eq(PrivateChat::getReceiveId, po.getPlayerAId())
            )

            .orderByDesc(PrivateChat::getCreateTime);

        return page(iPage, queryWrapper);
    }

    @Override
    public void add(PrivateChat po) {
        po.setCreateTime(LocalDateTime.now());
        po.setStatus(false);
        save(po);


        PlayerInfo senderPlayer = playerInfoService.getById(po.getSendId());
        PlayerInfo receiverPlayer = playerInfoService.getById(po.getReceiveId());
        PrivateChatResp playerInfoResp = BeanUtil.toBean(po, PrivateChatResp.class);

        // 发送方信息
        playerInfoResp.setSendAvatarPath(senderPlayer.getAvatarPath());
        playerInfoResp.setSendName(senderPlayer.getName());

        // 接收方信息
        playerInfoResp.setReceiveAvatarPath(receiverPlayer.getAvatarPath());
        playerInfoResp.setReceiveName(receiverPlayer.getName());

        messagingTemplate.convertAndSendToUser(po.getReceiveId() + "","/queue/private", playerInfoResp);
    }

    @Async
    @Override
    public void cleanNotRead(Long playerAId, Long playerBId) {
        UpdateWrapper<PrivateChat> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
            .and(
                query -> query
                    .eq(PrivateChat::getSendId, playerAId)
                    .eq(PrivateChat::getReceiveId, playerBId)
                    .or()
                    .eq(PrivateChat::getSendId, playerBId)
                    .eq(PrivateChat::getReceiveId, playerAId)
            )
            .eq(PrivateChat::getStatus, false)
            .set(PrivateChat::getStatus, true);

        update(updateWrapper);
    }

    @Override
    public int unreadCount(Long playerId) {
        QueryWrapper<PrivateChat> queryWrapper = new QueryWrapper<>();
        queryWrapper
            .lambda()
            .eq(PrivateChat::getReceiveId, playerId)
            .eq(PrivateChat::getStatus, false);
        return count(queryWrapper);
    }

    @Override
    public IPage<PrivateChat> adminPrivateChatPage(PlayerPrivateChatPageReq po) {

        IPage<PrivateChat> page = new Page<>(po.getPageNum(), po.getPageSize());

        QueryWrapper<PrivateChat> queryWrapper = new QueryWrapper<>();

        Long playerAId = po.getPlayerAId();
        Long playerBId = po.getPlayerBId();

        // ✅ 条件判断（外层if，不用lambda嵌套）
        if (playerAId != null && playerBId != null) {
            queryWrapper.lambda()
                    .and(q -> q
                            .eq(PrivateChat::getSendId, playerAId)
                            .eq(PrivateChat::getReceiveId, playerBId)
                    )
                    .or(q -> q
                            .eq(PrivateChat::getSendId, playerBId)
                            .eq(PrivateChat::getReceiveId, playerAId)
                    );

        } else if (playerAId != null) {

            queryWrapper.lambda()
                    .eq(PrivateChat::getSendId, playerAId)
                    .or()
                    .eq(PrivateChat::getReceiveId, playerAId);

        } else if (playerBId != null) {

            queryWrapper.lambda()
                    .eq(PrivateChat::getSendId, playerBId)
                    .or()
                    .eq(PrivateChat::getReceiveId, playerBId);
        }

        // ✅ 必须统一 return（不管有没有条件）
        queryWrapper.lambda().orderByDesc(PrivateChat::getCreateTime);

        return page(page, queryWrapper);
    }

    @Override
    public void cleanByPlayerIdAndTargetPlayerId(Long playerId, Long targetPlayerId) {
        LambdaQueryWrapper<PrivateChat> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(PrivateChat::getSendId, playerId)
                .eq(PrivateChat::getReceiveId, targetPlayerId);
        remove(queryWrapper);

        LambdaQueryWrapper<PrivateChat> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1
                .eq(PrivateChat::getSendId, targetPlayerId)
                .eq(PrivateChat::getReceiveId, playerId);
        remove(queryWrapper1);
    }

}
