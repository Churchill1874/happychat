package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.PrivateChat;
import com.ent.happychat.pojo.req.privatechat.PlayerPrivateChatPageReq;
import com.ent.happychat.pojo.req.privatechat.PrivateChatPageReq;

import java.util.List;

public interface PrivateChatService extends IService<PrivateChat> {

    List<PrivateChat> listByAccount(String account);

    IPage<PrivateChat> playerPrivateChatPage(PlayerPrivateChatPageReq po);

}
