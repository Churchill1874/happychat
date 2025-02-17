package com.ent.happychat.controller.player;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.constant.enums.LevelEnum;
import com.ent.happychat.common.constant.enums.UserStatusEnum;
import com.ent.happychat.common.exception.AccountOrPasswordException;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.tools.*;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.pojo.req.player.PersonalInfoUpdateReq;
import com.ent.happychat.pojo.req.player.PlayerLoginReq;
import com.ent.happychat.pojo.req.player.PlayerRegisterReq;
import com.ent.happychat.pojo.resp.player.PlayerInfoResp;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.service.EhcacheService;
import com.ent.happychat.service.PlayerInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@RestController
@Api(tags = "玩家")
@RequestMapping("/player/player")
public class PlayerApi {

    @Autowired
    private PlayerInfoService playerInfoService;
    @Autowired
    private EhcacheService ehcacheService;

    @PostMapping("/playerInfo")
    @ApiOperation(value = "玩家信息", notes = "玩家信息")
    public R<PlayerInfoResp> playerInfo() {
        String account = TokenTools.getPlayerToken(true).getAccount();
        PlayerInfo playerInfo = ehcacheService.playerInfoCache().get(account);
        if (playerInfo != null) {
            PlayerInfoResp playerInfoResp = BeanUtil.toBean(playerInfo, PlayerInfoResp.class);
            return R.ok(playerInfoResp);
        }

        playerInfo = playerInfoService.findByAccount(account);
        ehcacheService.playerInfoCache().put(account, playerInfo);
        PlayerInfoResp playerInfoResp = BeanUtil.toBean(playerInfo, PlayerInfoResp.class);
        return R.ok(playerInfoResp);
    }

    @PostMapping("/register")
    @ApiOperation(value = "注册", notes = "注册")
    public R<PlayerTokenResp> register(@RequestBody @Valid PlayerRegisterReq req) {
        log.info("玩家注册入参:{}", JSONUtil.toJsonStr(req));
        checkVerificationCode(req.getVerificationCode());

        CheckReqTools.account(req.getAccount());
        CheckReqTools.name(req.getName());
        CheckReqTools.password(req.getPassword());

        String salt = GenerateTools.getUUID();

        PlayerInfo playerInfo = new PlayerInfo();
        playerInfo.setIsBot(false);
        playerInfo.setAccount(req.getAccount());
        playerInfo.setName(req.getName());
        playerInfo.setPassword(CodeTools.md5AndSalt(req.getPassword(), salt));
        playerInfo.setSalt(salt);
        playerInfo.setPhone(req.getPhone());
        playerInfo.setCreateName("玩家");
        playerInfo.setCreateTime(LocalDateTime.now());
        playerInfo.setBirth(req.getBirth());
        playerInfo.setGender(req.getGender());
        playerInfo.setLevel(LevelEnum.LEVEL_0);
        playerInfo.setStatus(UserStatusEnum.NORMAL);
        playerInfo.setAvatarPath("1");
        playerInfo.setBalance(BigDecimal.ZERO);
        playerInfoService.add(playerInfo);

        PlayerTokenResp playerTokenResp = createLoginToken(playerInfo);
        return R.ok(playerTokenResp);
    }


    //创建登录token
    private PlayerTokenResp createLoginToken(PlayerInfo playerInfo) {
        String tokenId = GenerateTools.createTokenId();
        PlayerTokenResp playerTokenResp = BeanUtil.toBean(playerInfo, PlayerTokenResp.class);
        playerTokenResp.setTokenId(tokenId);
        playerTokenResp.setLoginTime(LocalDateTime.now());
        playerTokenResp.setId(playerInfo.getId());
        playerTokenResp.setAvatarPath(playerInfo.getAvatarPath());
        playerTokenResp.setLevel(playerInfo.getLevel());

        ehcacheService.playerTokenCache().put(tokenId, playerTokenResp);
        ehcacheService.playerInfoCache().put(playerInfo.getAccount(), playerInfo);
        return playerTokenResp;
    }


    //校验验证码
    private void checkVerificationCode(String reqVerificationCode) {
        String verificationCode = ehcacheService.verificationCache().get(HttpTools.getIp());
        if (verificationCode == null) {
            throw new DataException("验证码有误或已过期");
        }
        if (!verificationCode.equalsIgnoreCase(reqVerificationCode)) {
            throw new DataException("验证码错误");
        }
    }


    @PostMapping("/login")
    @ApiOperation(value = "登录", notes = "登录")
    public R<PlayerTokenResp> login(@RequestBody @Valid PlayerLoginReq req) {
        log.info("玩家登录入参:{}", JSONUtil.toJsonStr(req));
        checkVerificationCode(req.getVerificationCode());

        CheckReqTools.password(req.getPassword());
        CheckReqTools.account(req.getAccount());

        if (StringUtils.isNotBlank(req.getAccount()) && req.getAccount().length() > 20) {
            throw new DataException("账号输入过长");
        }

        //根据登录方式查询账号
        PlayerInfo playerInfo = playerInfoService.findByAccount(req.getAccount());
        if (playerInfo == null) {
            throw new AccountOrPasswordException();
        }
        if (!playerInfo.getPassword().equals(CodeTools.md5AndSalt(req.getPassword(), playerInfo.getSalt()))) {
            throw new AccountOrPasswordException();
        }

        PlayerTokenResp playerTokenResp = createLoginToken(playerInfo);
        return R.ok(playerTokenResp);
    }


    @PostMapping("/logout")
    @ApiOperation(value = "退出登录", notes = "退出登录")
    public R logout() {
        ehcacheService.playerTokenCache().remove(TokenTools.getPlayerToken(true).getTokenId());
        return R.ok(null);
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新", notes = "更新")
    public R update(@RequestBody @Valid PersonalInfoUpdateReq req) {
        CheckReqTools.name(req.getName());

        PlayerTokenResp playerTokenResp = TokenTools.getPlayerToken(true);

        PlayerInfo playerInfo = playerInfoService.findByAccount(playerTokenResp.getAccount());
        if (playerInfo == null) {
            throw new DataException("数据异常");
        }
        playerInfo.setName(req.getName());
        playerInfo.setCity(req.getCity());
        playerInfo.setPhone(req.getPhone());
        playerInfo.setEmail(req.getEmail());
        playerInfo.setSelfIntroduction(req.getSelfIntroduction());
        playerInfo.setAvatarPath(req.getAvatarPath());
        playerInfo.setUpdateName(playerTokenResp.getName());
        playerInfo.setUpdateTime(LocalDateTime.now());
        playerInfoService.updateById(playerInfo);


        playerTokenResp.setName(playerInfo.getName());
        playerTokenResp.setAvatarPath(playerInfo.getAvatarPath());
        playerTokenResp.setLevel(playerInfo.getLevel());
        //更新token缓存
        ehcacheService.playerTokenCache().put(playerTokenResp.getTokenId(), playerTokenResp);

        //更新账号关联的缓存
        ehcacheService.playerInfoCache().remove(playerInfo.getAccount());
        return R.ok(null);
    }
}
