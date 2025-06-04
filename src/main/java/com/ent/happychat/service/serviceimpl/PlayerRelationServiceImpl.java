package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.entity.PlayerRelation;
import com.ent.happychat.mapper.PlayerRelationMapper;
import com.ent.happychat.pojo.req.playerrelation.PlayerRelationPageReq;
import com.ent.happychat.service.PlayerRelationService;
import org.springframework.stereotype.Service;

@Service
public class PlayerRelationServiceImpl extends ServiceImpl<PlayerRelationMapper, PlayerRelation> implements PlayerRelationService {

    @Override
    public void add(PlayerRelation playerRelation) {
        save(playerRelation);
    }

    @Override
    public IPage<PlayerRelation> queryPage(PlayerRelationPageReq dto) {
        IPage<PlayerRelation> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<PlayerRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
            .eq(dto.getPlayerId() != null, PlayerRelation::getPlayerId, dto.getPlayerId())
            .eq(dto.getTargetPlayerId() != null, PlayerRelation::getTargetPlayerId, dto.getTargetPlayerId())
            .orderByDesc(PlayerRelation::getCreateTime);
        return page(iPage, queryWrapper);
    }

}
