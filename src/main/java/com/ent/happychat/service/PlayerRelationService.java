package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.PlayerRelation;
import com.ent.happychat.pojo.req.playerrelation.PlayerRelationPageReq;

public interface PlayerRelationService extends IService<PlayerRelation> {

    void add(PlayerRelation playerRelation);

    IPage<PlayerRelation> queryPage(PlayerRelationPageReq dto);

}
