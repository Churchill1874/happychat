package com.ent.happychat.controller.player;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.tools.HttpTools;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.Politics;
import com.ent.happychat.pojo.req.IdBase;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.service.EhcacheService;
import com.ent.happychat.service.PoliticsService;
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
@Api(tags = "政治")
@RequestMapping("/player/politics")
public class PoliticsApi {

    @Autowired
    private PoliticsService politicsService;
    @Autowired
    private EhcacheService ehcacheService;

    @PostMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<IPage<Politics>> page(@RequestBody @Valid PageBase req) {
        IPage<Politics> iPage = politicsService.queryPage(req);
        return R.ok(iPage);
    }

    @PostMapping("/find")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<Politics> find(@RequestBody @Valid IdBase req) {
        PlayerTokenResp playerTokenResp = TokenTools.getPlayerToken(false);
        Long playerId = null;
        String playerName = null;
        if (playerTokenResp != null){
            playerId = playerTokenResp.getId();
            playerName = playerTokenResp.getName();
        }
        Politics politics = politicsService.findByIdAndInsertRecord(HttpTools.getIp(), req.getId() ,playerId, playerName);
        return R.ok(politics);
    }


}
