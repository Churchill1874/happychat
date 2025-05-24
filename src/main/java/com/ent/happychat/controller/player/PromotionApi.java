package com.ent.happychat.controller.player;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.tools.HttpTools;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.Promotion;
import com.ent.happychat.entity.Society;
import com.ent.happychat.pojo.req.IdBase;
import com.ent.happychat.pojo.req.promotion.PromotionPageReq;
import com.ent.happychat.pojo.req.society.SocietyPageReq;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.service.PromotionService;
import com.ent.happychat.service.SocietyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@Api(tags = "推广")
@RequestMapping("/player/promotion")
public class PromotionApi {

    @Autowired
    private PromotionService promotionService;


    @PostMapping("/queryPage")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<IPage<Promotion>> queryPage(@RequestBody PromotionPageReq req) {
        IPage<Promotion> iPage = promotionService.queryPage(req);
        return R.ok(iPage);
    }

    @PostMapping("/find")
    @ApiOperation(value = "根据id查询", notes = "根据id查询")
    public R<Promotion> find(@RequestBody @Valid IdBase req) {
        PlayerTokenResp playerTokenResp = TokenTools.getPlayerToken(false);
        Long playerId = null;
        String playerName = null;
        if (playerTokenResp != null){
            playerId = playerTokenResp.getId();
            playerName = playerTokenResp.getName();
        }
        Promotion promotion = promotionService.findByIdAndInsertRecord(HttpTools.getIp(), req.getId() ,playerId, playerName);
        return R.ok(promotion);
    }


}
