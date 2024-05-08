package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.pojo.req.PageBase;

public interface PlayerInfoService extends IService<PlayerInfo> {

    IPage<PlayerInfo> queryPage(PlayerInfo playerInfo, PageBase pageBase);

    void add(PlayerInfo playerInfo);

}
