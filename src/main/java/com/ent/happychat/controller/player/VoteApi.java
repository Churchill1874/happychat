package com.ent.happychat.controller.player;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.tools.HttpTools;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.pojo.req.IdBase;
import com.ent.happychat.pojo.req.vote.VotePageReq;
import com.ent.happychat.pojo.req.vote.VoteSubmitReq;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.pojo.resp.vote.VoteResp;
import com.ent.happychat.service.EhcacheService;
import com.ent.happychat.service.VoteService;
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
@Api(tags = "投票")
@RequestMapping("/player/vote")
public class VoteApi {

    @Autowired
    private VoteService voteService;
    @Autowired
    private EhcacheService ehcacheService;

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页查询（只返回展示中的投票，带选项和票数）", notes = "分页查询")
    public R<IPage<VoteResp>> queryPage(@RequestBody VotePageReq req) {
        return R.ok(voteService.queryPage(req, true));
    }

    @PostMapping("/find")
    @ApiOperation(value = "根据id查询详情，登录时会返回自己已选的选项", notes = "根据id查询")
    public R<VoteResp> find(@RequestBody @Valid IdBase req) {
        PlayerTokenResp player = TokenTools.getPlayerToken(false);
        Long playerId = player == null ? null : player.getId();
        String playerName = player == null ? null : player.getName();
        return R.ok(voteService.findByIdAndInsertRecord(HttpTools.getIp(), req.getId(), playerId, playerName));
    }

    @PostMapping("/submit")
    @ApiOperation(value = "投票 / 改票（截止前可改）", notes = "需要登录")
    public R<VoteResp> submit(@RequestBody @Valid VoteSubmitReq req) {
        PlayerTokenResp player = TokenTools.getPlayerToken(true);
        ehcacheService.verification3SecondsRequest(player.getAccount() + ":vote");
        return R.ok(voteService.submit(req, player));
    }

}
