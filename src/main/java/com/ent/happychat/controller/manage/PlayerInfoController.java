package com.ent.happychat.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.constant.enums.LevelTypeEnum;
import com.ent.happychat.common.constant.enums.UserStatusEnum;
import com.ent.happychat.common.tools.CheckReqTools;
import com.ent.happychat.common.tools.CodeTools;
import com.ent.happychat.common.tools.GenerateTools;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.pojo.req.Id;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.pojo.req.UpdateStatusBase;
import com.ent.happychat.pojo.req.player.PlayerInfoAddReq;
import com.ent.happychat.pojo.req.player.PlayerInfoPageReq;
import com.ent.happychat.pojo.req.player.PlayerInfoUpdateReq;
import com.ent.happychat.service.PlayerInfoService;
import com.google.common.collect.Lists;
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
import java.util.List;

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
        CheckReqTools.account(req.getAccount());
        CheckReqTools.name(req.getName());

        PlayerInfo playerInfo = BeanUtil.toBean(req, PlayerInfo.class);
        if (req.getLevel() == null) {
            req.setLevel(LevelTypeEnum.LEVEL_0);
        }
        if (req.getIsBot() == null) {
            req.setIsBot(true);
        }

        String salt = GenerateTools.getUUID();
        playerInfo.setStatus(UserStatusEnum.NORMAL);
        playerInfo.setPassword(CodeTools.md5AndSalt(req.getPassword(),salt));
        playerInfo.setSalt(salt);
        playerInfo.setCreateName(TokenTools.getAdminToken().getName());
        playerInfo.setCreateTime(LocalDateTime.now());
        playerInfoService.add(playerInfo);
        return R.ok(null);
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新", notes = "更新")
    public R update(@RequestBody @Valid PlayerInfoUpdateReq req) {
        CheckReqTools.name(req.getName());

        PlayerInfo playerInfo = BeanUtil.toBean(req, PlayerInfo.class);
        if (req.getLevel() == null) {
            req.setLevel(LevelTypeEnum.LEVEL_0);
        }
        playerInfo.setUpdateName(TokenTools.getAdminToken().getName());
        playerInfo.setUpdateTime(LocalDateTime.now());
        playerInfoService.updateById(playerInfo);
        return R.ok(null);
    }

    @PostMapping("/updateStatus")
    @ApiOperation(value = "修改状态", notes = "修改状态")
    public R updateStatus(@RequestBody @Valid UpdateStatusBase req) {
        playerInfoService.updateStatus(req.getId(), req.getStatus());
        return R.ok(null);
    }

    @PostMapping("/levelTypeList")
    @ApiOperation(value = "枚举类型列表", notes = "枚举类型列表")
    public R<List<LevelTypeEnum>> levelTypeList() {
        return R.ok(Lists.newArrayList(LevelTypeEnum.values()));
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除", notes = "删除")
    public R delete(@RequestBody @Valid Id req) {
        playerInfoService.removeById(req.getId());
        return R.ok(null);
    }

}
