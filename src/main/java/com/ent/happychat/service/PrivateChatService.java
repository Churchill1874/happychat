package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.PrivateChat;
import com.ent.happychat.pojo.req.privatechat.PlayerPrivateChatPageReq;

import java.util.List;

public interface PrivateChatService extends IService<PrivateChat> {

    List<PrivateChat> listByAccount(String account);

    IPage<PrivateChat> playerPrivateChatPage(PlayerPrivateChatPageReq po);

    void add(PrivateChat po);

    void cleanNotRead(String account1, String account2);

}
