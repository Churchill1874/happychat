package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.PlayerRelation;
import com.ent.happychat.pojo.req.playerrelation.PlayerRelationPageReq;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PlayerRelationService extends IService<PlayerRelation> {

    void add(PlayerRelation playerRelation, String content);

    void delete(Long playerId, Long targetPlayerId);

    IPage<PlayerRelation> queryPage(PlayerRelationPageReq dto);

    PlayerRelation queryRelation(Long playerId, Long targetPlayerId);

    Set<Long> relationSet(Long playerId, List<Long> targetPlayerIdList);

}
