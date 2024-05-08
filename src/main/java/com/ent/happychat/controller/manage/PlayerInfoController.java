package com.ent.happychat.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.tools.CodeTools;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.pojo.req.Id;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.pojo.req.player.PlayerInfoAddReq;
import com.ent.happychat.pojo.req.player.PlayerInfoPageReq;
import com.ent.happychat.service.PlayerInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Slf4j
@RestController
@Api(tags = "玩家")
@RequestMapping("/manage/playerInfo")
public class PlayerInfoController {

    @Autowired
    private PlayerInfoService playerInfoService;

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页", notes = "分页")
    public R<IPage<PlayerInfo>> queryPage(@RequestBody @Valid PlayerInfoPageReq req) {
        PlayerInfo playerInfo = BeanUtil.toBean(req, PlayerInfo.class);
        PageBase pageBase = BeanUtil.toBean(req, PageBase.class);
        IPage<PlayerInfo> iPage = playerInfoService.queryPage(playerInfo, pageBase);
        return R.ok(iPage);
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增", notes = "新增")
    public R add(@RequestBody @Valid PlayerInfoAddReq req) {
        PlayerInfo playerInfo = BeanUtil.toBean(req, PlayerInfo.class);
        if (req.getLevel() == null) {
            req.setLevel(0);
        }
        if (req.getIsBot() == null) {
            req.setIsBot(true);
        }
        playerInfo.setPassword(CodeTools.md5AndSalt(req.getPassword()));
        playerInfo.setCreateName(TokenTools.getAdminToken().getName());
        playerInfo.setCreateTime(LocalDateTime.now());
        playerInfoService.add(playerInfo);
        return R.ok(null);
    }


    @PostMapping("/delete")
    @ApiOperation(value = "删除", notes = "删除")
    public R delete(@RequestBody @Valid Id req) {
        playerInfoService.removeById(req.getId());
        return R.ok(null);
    }

}
