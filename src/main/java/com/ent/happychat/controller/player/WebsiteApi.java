package com.ent.happychat.controller.player;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.constant.enums.UserStatusEnum;
import com.ent.happychat.common.exception.AccountOrPasswordException;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.exception.TokenException;
import com.ent.happychat.common.tools.CodeTools;
import com.ent.happychat.common.tools.GenerateTools;
import com.ent.happychat.common.tools.HttpTools;
import com.ent.happychat.entity.Player;
import com.ent.happychat.pojo.req.website.LoginPlayer;
import com.ent.happychat.pojo.req.website.Register;
import com.ent.happychat.pojo.dto.PlayerToken;
import com.ent.happychat.service.EhcacheService;
import com.ent.happychat.service.PlayerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "登录")
@RequestMapping("/player/website")
public class WebsiteApi {
    @Autowired
    private EhcacheService ehcacheService;
    @Autowired
    private PlayerService playerService;

    @PostMapping("/register")
    @ApiOperation(value = "注册")
    public synchronized R<Player> register(@RequestBody @Valid Register req) {
        log.info("注册请求参数:{}", JSONObject.toJSONString(req));
        //校验图形验证码
        //checkGraphicVerificationCode(req.getVerificationCode());

        Player player = playerService.findByName(req.getName());
        if (player != null) {
            return R.failed("该用户名已被注册");
        }

        player = playerService.findByAccount(req.getAccount());
        if (player != null) {
            return R.failed("该账号已经存在");
        }

        //todo 不需要先临时注释
        //校验短信验证码
        //checkSmsVerificationCode(req.getPhoneNumber(), req.getSmsVerificationCode());

        //校验ip总注册数量是否超过10个
        //checkRegister(10);

        //创建用户数据
        player = new Player();
        BeanUtils.copyProperties(req, player);

        String passwordReq = CodeTools.md5AndSalt(req.getPassword());
        player.setAccount(req.getAccount());
        player.setPassword(passwordReq);
        player.setBalance(BigDecimal.ZERO);
        player.setLevel(1);
        player.setName(req.getName());
        player.setAddress(HttpTools.getAddress());
        player.setStatus(UserStatusEnum.NORMAL);
        player.setCreateTime(LocalDateTime.now());
        player.setIsRobot(false);
        playerService.add(player);

        //记录登录日志
        //logRecordService.insert(GenerateTools.registerLog(user.getName(), user.getAccount()));

        return R.ok(null);
    }

    @PostMapping("/login")
    @ApiOperation(value = "登录", notes = "手机号或账号 至少有一个有值")
    public R<String> login(@RequestBody @Valid LoginPlayer req) {
        //校验请求参数
        if (req.getAccount() == null) {
            throw new DataException("账号不能同时为空");
        }

        //校验图形验证码
        //checkGraphicVerificationCode(req.getVerificationCode());

        //判断账号密码是否正确
        Player player = null;
        if (req.getAccount() != null) {
            player = playerService.findByAccount(req.getAccount());
        }

        if (player == null) {
            throw new AccountOrPasswordException();
        }

        //对比登录密码和正确密码
        String password = player.getPassword();
        String passwordReq = CodeTools.md5AndSalt(req.getPassword());

        //如果填写的登录密码是错误的
        if (!password.equals(passwordReq)) {
            throw new AccountOrPasswordException();
        }


        //如果已经登陆过,删除之前的tokenId和缓存
        this.checkLoginCache(player.getAccount());

        //生成token并返回
        PlayerToken playerToken = GenerateTools.createToken(player);
        String tokenId = GenerateTools.createTokenId(player.getAccount());
        ehcacheService.getPlayerTokenCache().put(tokenId, playerToken);

        //删除使用过的验证码缓存
        ehcacheService.getVerificationCodeCache().evict(HttpTools.getIp());
        return R.ok(tokenId);
    }


    //如果当前登录的账号已经是登陆状态 则删除之前的登录缓存
    private void checkLoginCache(String account) {
        Cache cache = ehcacheService.getPlayerTokenCache();
        if (cache == null){
            throw new TokenException();
        }
        net.sf.ehcache.Cache c = (net.sf.ehcache.Cache) cache.getNativeCache();
        List<String> list = c.getKeys();
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(tokenId -> {
                if (tokenId.contains(String.valueOf(account))) {
                    ehcacheService.getPlayerTokenCache().evict(tokenId);
                }
            });
        }
    }

    /**
     * 校验图形验证码
     */
    private void checkGraphicVerificationCode(String verificationCodeReq) {
        String verificationCode = ehcacheService.getVerificationCodeCache().get(HttpTools.getIp(), String.class);
        if (StringUtils.isBlank(verificationCode) || !verificationCode.equals(verificationCodeReq)) {
            throw new DataException("验证码有误或已过期");
        }
    }

    /**
     * 校验短信验证码
     */
    private void checkSmsVerificationCode(String phoneNumber, String verificationCodeReq) {
        String smsVerificationCode = ehcacheService.getSmsVerificationCodeCache().get(phoneNumber, String.class);
        if (StringUtils.isBlank(smsVerificationCode) || !smsVerificationCode.equals(verificationCodeReq)) {
            throw new DataException("验证码有误或已过期");
        }
    }

    @PostMapping("/根据ip获取地址")
    @ApiOperation(value = "根据ip获取地址")
    public R<String> getAddressByIp(@RequestBody JSONObject json) {
        return R.ok(HttpTools.findAddressByIp(""));
    }

}
