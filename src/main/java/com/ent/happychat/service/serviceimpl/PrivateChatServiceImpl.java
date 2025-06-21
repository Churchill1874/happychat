package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.entity.PrivateChat;
import com.ent.happychat.mapper.PrivateChatMapper;
import com.ent.happychat.pojo.req.privatechat.PlayerPrivateChatPageReq;
import com.ent.happychat.service.PrivateChatService;
import lombok.extern.slf4j.Slf4j;
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
        messagingTemplate.convertAndSendToUser(po.getReceiveId() + "","/queue/private", po);
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

}
