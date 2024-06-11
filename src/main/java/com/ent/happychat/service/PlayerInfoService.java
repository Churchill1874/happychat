package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.pojo.req.PageBase;

public interface PlayerInfoService extends IService<PlayerInfo> {

    IPage<PlayerInfo> queryPage(PlayerInfo playerInfo, PageBase pageBase);

    void add(PlayerInfo playerInfo);

    PlayerInfo findByAccount(String account);

    void updateStatus(Long id , Boolean status);

    PlayerInfo findByLogin(String account, String name, String phone, String email);

    PlayerInfo findByName(String name);
}
