package com.ent.happychat.controller.player;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.entity.PlayerRelation;
import com.ent.happychat.pojo.req.playerrelation.PlayerRelationAddReq;
import com.ent.happychat.pojo.req.playerrelation.PlayerRelationPageReq;
import com.ent.happychat.pojo.resp.player.PlayerInfoResp;
import com.ent.happychat.service.PlayerInfoService;
import com.ent.happychat.service.PlayerRelationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Api(tags = "玩家关系")
@RequestMapping("/player/relation")
public class PlayerRelationApi {
    @Autowired
    private PlayerRelationService playerRelationService;
    @Autowired
    private PlayerInfoService playerInfoService;

    @PostMapping("/pageTargetPlayerId")
    @ApiOperation(value = "被点赞分页查询", notes = "被点赞分页查询")
    public R<IPage<PlayerInfoResp>> page(@RequestBody PlayerRelationPageReq req) {
        if (req.getTargetPlayerId() == null) {
            return R.ok(null);
        }
        req.setPlayerId(null);

        IPage<PlayerRelation> iPage = playerRelationService.queryPage(req);

        IPage<PlayerInfoResp> emptyPage = new Page<>(req.getPageNum(),req.getPageSize());
        if (CollectionUtils.isEmpty(iPage.getRecords())){
            return R.ok(emptyPage);
        }

        List<Long> playerIdList = iPage.getRecords().stream().map(PlayerRelation::getPlayerId).collect(Collectors.toList());

        Map<Long, PlayerInfo> map = playerInfoService.mapByIds(playerIdList);

        List<PlayerInfoResp> playerInfoList = new ArrayList<>();
        for(PlayerRelation playerRelation : iPage.getRecords()){
            PlayerInfo playerInfo = map.get(playerRelation.getPlayerId());
            if (playerInfo != null){
                playerInfoList.add(BeanUtil.toBean(playerInfo, PlayerInfoResp.class));
            }
        }

        emptyPage.setPages(iPage.getPages());
        emptyPage.setTotal(iPage.getTotal());
        emptyPage.setRecords(playerInfoList);

        return R.ok(emptyPage);
    }

    @PostMapping("/pagePlayerId")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<IPage<PlayerInfoResp>> pagePlayerId(@RequestBody PlayerRelationPageReq req) {
        if (req.getPlayerId() == null) {
            return R.ok(null);
        }
        req.setTargetPlayerId(null);

        IPage<PlayerRelation> iPage = playerRelationService.queryPage(req);

        IPage<PlayerInfoResp> emptyPage = new Page<>(req.getPageNum(),req.getPageSize());
        if (CollectionUtils.isEmpty(iPage.getRecords())){
            return R.ok(emptyPage);
        }

        List<Long> targetPlayerIdList = iPage.getRecords().stream().map(PlayerRelation::getTargetPlayerId).collect(Collectors.toList());

        Map<Long, PlayerInfo> map = playerInfoService.mapByIds(targetPlayerIdList);

        List<PlayerInfoResp> playerInfoList = new ArrayList<>();
        for(PlayerRelation playerRelation : iPage.getRecords()){
            PlayerInfo playerInfo = map.get(playerRelation.getTargetPlayerId());
            if (playerInfo != null){
                playerInfoList.add(BeanUtil.toBean(playerInfo, PlayerInfoResp.class));
            }
        }

        emptyPage.setPages(iPage.getPages());
        emptyPage.setTotal(iPage.getTotal());
        emptyPage.setRecords(playerInfoList);

        return R.ok(emptyPage);
    }



    @PostMapping("/add")
    @ApiOperation(value = "添加", notes = "添加")
    public R page(@RequestBody @Valid PlayerRelationAddReq req) {
        playerRelationService.add(BeanUtil.toBean(req, PlayerRelation.class));
        return R.ok(null);
    }

}
