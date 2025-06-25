package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.PrivateChat;
import com.ent.happychat.pojo.req.privatechat.PlayerPrivateChatPageReq;

import java.util.List;

public interface PrivateChatService extends IService<PrivateChat> {

    List<PrivateChat> listByAccountId(Long playerId);

    IPage<PrivateChat> playerPrivateChatPage(PlayerPrivateChatPageReq po);

    void add(PrivateChat po);

    void cleanNotRead(Long playerAId, Long playerBId);

    int unreadCount(Long playerId);


}
