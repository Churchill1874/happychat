package com.ent.happychat.controller.player;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.constant.SystemConstant;
import com.ent.happychat.common.constant.enums.CacheTypeEnum;
import com.ent.happychat.common.exception.AccountOrPasswordException;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.tools.CheckReqTools;
import com.ent.happychat.common.tools.CodeTools;
import com.ent.happychat.common.tools.GenerateTools;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.pojo.req.player.PlayerLoginReq;
import com.ent.happychat.pojo.req.player.PlayerRegisterReq;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.service.EhcacheService;
import com.ent.happychat.service.PlayerInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@RestController
@Api(tags = "新闻")
@RequestMapping("/player/player")
public class PlayerApi {

    @Autowired
    private PlayerInfoService playerInfoService;
    @Autowired
    private EhcacheService ehcacheService;

    @PostMapping("/onlineCount")
    @ApiOperation(value = "在线人数", notes = "在线人数")
    public R<Integer> onlineCount() {
        Cache cache = ehcacheService.getCache(CacheTypeEnum.PLAYER_TOKEN);

        return R.ok(0);
    }

    @PostMapping("/register")
    @ApiOperation(value = "注册", notes = "注册")
    public R<PlayerTokenResp> register(@RequestBody @Valid PlayerRegisterReq req) {
        log.info("玩家注册入参:{}", JSONUtil.toJsonStr(req));

        CheckReqTools.account(req.getAccount());
        CheckReqTools.name(req.getName());
        CheckReqTools.password(req.getPassword());

        String salt = GenerateTools.getUUID();

        PlayerInfo playerInfo = new PlayerInfo();
        playerInfo.setAccount(req.getAccount());
        playerInfo.setName(req.getName());
        playerInfo.setPassword(CodeTools.md5AndSalt(req.getPassword(), salt));
        playerInfo.setSalt(salt);
        playerInfo.setPhone(req.getPhone());
        playerInfo.setCreateName("玩家");
        playerInfo.setCreateTime(LocalDateTime.now());
        playerInfo.setBirth(req.getBirth());
        playerInfo.setGender(req.getGender());
        playerInfoService.save(playerInfo);

        PlayerTokenResp playerTokenResp = createLoginToken(playerInfo);
        return R.ok(playerTokenResp);
    }


    //创建登录token
    private PlayerTokenResp createLoginToken(PlayerInfo playerInfo) {
        String tokenId = GenerateTools.createTokenId();
        PlayerTokenResp playerTokenResp = BeanUtil.toBean(playerInfo, PlayerTokenResp.class);
        playerTokenResp.setTokenId(tokenId);
        playerTokenResp.setLoginTime(LocalDateTime.now());

        ehcacheService.getCache(CacheTypeEnum.PLAYER_TOKEN).put(tokenId, playerTokenResp);
        return playerTokenResp;
    }


    @PostMapping("/login")
    @ApiOperation(value = "登录", notes = "登录")
    public R<PlayerTokenResp> login(@RequestBody @Valid PlayerLoginReq req) {
        log.info("玩家登录入参:{}", JSONUtil.toJsonStr(req));
        CheckReqTools.password(req.getPassword());
        CheckReqTools.account(req.getAccount());
/*        if (StringUtils.isAnyBlank(req.getPhone(), req.getName(), req.getEmail(), req.getAccount())) {
            throw new DataException("登录方式不能为空");
        }
        if (StringUtils.isNotBlank(req.getEmail()) && req.getEmail().length() > 50) {
            throw new DataException("邮箱长度输入过长");
        }
        if (StringUtils.isNotBlank(req.getPhone()) && req.getPhone().length() > 20) {
            throw new DataException("手机号输入过长");
        }
        if (StringUtils.isNotBlank(req.getName()) && req.getName().length() > 20) {
            throw new DataException("昵称输入过长");
        }*/
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
}
