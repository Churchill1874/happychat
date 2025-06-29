package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.ChatRoom;
import com.ent.happychat.pojo.req.PageAndIdReq;

public interface ChatRoomService extends IService<ChatRoom> {

    IPage<ChatRoom> queryPage(PageAndIdReq dto);

    void send(ChatRoom dto);

}
