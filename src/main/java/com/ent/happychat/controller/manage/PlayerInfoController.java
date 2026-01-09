package com.ent.happychat.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.annotation.AdminLoginCheck;
import com.ent.happychat.common.constant.enums.LevelEnum;
import com.ent.happychat.common.constant.enums.UserStatusEnum;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.tools.CheckReqTools;
import com.ent.happychat.common.tools.CodeTools;
import com.ent.happychat.common.tools.GenerateTools;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.pojo.req.IdBase;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.pojo.req.UpdateStatusBase;
import com.ent.happychat.pojo.req.player.PlayerInfoAddReq;
import com.ent.happychat.pojo.req.player.PlayerInfoPageReq;
import com.ent.happychat.pojo.req.player.PlayerInfoUpdateReq;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.service.EhcacheService;
import com.ent.happychat.service.PlayerInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "玩家")
@RequestMapping("/manage/playerInfo")
public class PlayerInfoController {

    @Autowired
    private EhcacheService ehcacheService;
    @Autowired
    private PlayerInfoService playerInfoService;

    @PostMapping("/onlinePlayerList")
    @ApiOperation(value = "在线玩家信息", notes = "在线玩家信息")
    public R<List<PlayerTokenResp>> onlinePlayerList() {
        List<PlayerTokenResp> list = new ArrayList<>();
        Iterator<Cache.Entry<String, PlayerTokenResp>> iterator = ehcacheService.onlineCountCache().iterator();

        while (iterator.hasNext()) {
            Cache.Entry<String, PlayerTokenResp> entry = iterator.next();
            list.add(entry.getValue());
        }

        return R.ok(list);
    }

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页", notes = "分页")
    public R<IPage<PlayerInfo>> queryPage(@RequestBody @Valid PlayerInfoPageReq req) {
        PlayerInfo playerInfo = BeanUtil.toBean(req, PlayerInfo.class);
        PageBase pageBase = BeanUtil.toBean(req, PageBase.class);
        IPage<PlayerInfo> iPage = playerInfoService.queryPage(playerInfo, pageBase);
        return R.ok(iPage);
    }

    @PostMapping("/add")
    @AdminLoginCheck
    @ApiOperation(value = "新增", notes = "新增")
    public R add(@RequestBody @Valid PlayerInfoAddReq req) {
        CheckReqTools.account(req.getAccount());
        CheckReqTools.name(req.getName());

        PlayerInfo playerInfo = BeanUtil.toBean(req, PlayerInfo.class);
        if (req.getLevel() == null) {
            playerInfo.setLevel(LevelEnum.LEVEL_0);
        }
        if (req.getIsBot() == null) {
            playerInfo.setIsBot(true);
        }

        String salt = GenerateTools.getUUID();
        playerInfo.setStatus(UserStatusEnum.NORMAL);
        playerInfo.setPassword(CodeTools.md5AndSalt(req.getPassword(), salt));
        playerInfo.setSalt(salt);
        playerInfo.setCreateName(TokenTools.getAdminToken(true).getName());
        playerInfo.setCreateTime(LocalDateTime.now());
        playerInfo.setBalance(BigDecimal.ZERO);
        playerInfoService.add(playerInfo);
        return R.ok(null);
    }

    @PostMapping("/update")
    @AdminLoginCheck
    @ApiOperation(value = "更新", notes = "更新")
    public R update(@RequestBody @Valid PlayerInfoUpdateReq req) {
        CheckReqTools.name(req.getName());

        PlayerInfo playerInfo = playerInfoService.getById(req.getId());
        if (playerInfo == null){
            throw new DataException("数据异常");
        }

        playerInfo.setName(req.getName());
        playerInfo.setPhone(req.getPhone());
        playerInfo.setEmail(req.getEmail());
        playerInfo.setGender(req.getGender());
        playerInfo.setCity(req.getCity());
        playerInfo.setBirth(req.getBirth());
        playerInfo.setLevel(req.getLevel());
        playerInfo.setSelfIntroduction(req.getSelfIntroduction());
        playerInfo.setAvatarPath(req.getAvatarPath());
        playerInfo.setBalance(req.getBalance());

        if (req.getLevel() == null) {
            playerInfo.setLevel(LevelEnum.LEVEL_0);
        }
        playerInfo.setUpdateName(TokenTools.getAdminToken(true).getName());
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
    public R<List<LevelEnum.LevelType>> levelTypeList() {
        List<LevelEnum.LevelType> list = new ArrayList<>();
        for (LevelEnum levelEnum : LevelEnum.values()) {
            LevelEnum.LevelType levelType = new LevelEnum.LevelType();
            levelType.setCode(levelEnum.getCode());
            levelType.setName(levelEnum.getName());
            levelType.setCorrectCount(levelEnum.getCorrectCount());
            levelType.setLikesReceivedCount(levelEnum.getLikesReceivedCount());
            list.add(levelType);
        }

        return R.ok(list);
    }

    @PostMapping("/delete")
    @AdminLoginCheck
    @ApiOperation(value = "删除", notes = "删除")
    public R delete(@RequestBody @Valid IdBase req) {
        playerInfoService.removeById(req.getId());
        return R.ok(null);
    }

    @PostMapping("/findById")
    @ApiOperation(value = "根据id查询", notes = "根据id查询")
    public R<PlayerInfo> findById(@RequestBody @Valid IdBase req) {
        PlayerInfo playerInfo = playerInfoService.getById(req.getId());
        return R.ok(playerInfo);
    }


}
