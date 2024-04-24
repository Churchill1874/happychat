package com.ent.happychat.controller.manage;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.exception.AccountOrPasswordException;
import com.ent.happychat.common.tools.CodeTools;
import com.ent.happychat.common.tools.GenerateTools;
import com.ent.happychat.common.tools.HttpTools;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.Administrators;
import com.ent.happychat.pojo.dto.AdminToken;
import com.ent.happychat.pojo.req.website.LoginManage;
import com.ent.happychat.service.AdministratorsService;
import com.ent.happychat.service.EhcacheService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
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
@Api(tags = "登录")
@RequestMapping("/manage/website")
public class WebsiteController {

    @Autowired
    private EhcacheService ehcacheService;

    @Autowired
    private AdministratorsService administratorsService;

    @PostMapping("/login")
    @ApiOperation(value = "登录", notes = "登录")
    public R<AdminToken> login(@RequestBody @Valid LoginManage req) {
        log.info("登录接口入参:{}", JSONUtil.toJsonStr(req));

        //校验验证码
        String verificationCode = ehcacheService.getVerificationCodeCache().get(HttpTools.getIp(), String.class);
        if (verificationCode == null) {
            return R.failed("验证码有误或已过期");
        }

        //判断账号密码是否正确
        Administrators administrators = administratorsService.findByAccount(req.getAccount());
        if (administrators == null) {
            throw new AccountOrPasswordException();
        }

        //对比登录密码和正确密码
        String password = administrators.getPassword();
        String passwordReq = CodeTools.md5AndSalt(req.getPassword());

        if (!password.equals(passwordReq)) {
            throw new AccountOrPasswordException();
        }

        //校验是否已经登录,如果已经登陆过删除之前的tokenId和缓存
        //checkLoginCache(administrators.getAccount());

        String tokenId = GenerateTools.createTokenId(administrators.getAccount());
        //生成token并返回
        AdminToken adminToken = new AdminToken();
        adminToken.setAccount(req.getAccount());
        adminToken.setName(administrators.getName());
        adminToken.setRole(administrators.getRole());
        adminToken.setLoginTime(LocalDateTime.now());
        adminToken.setTokenId(tokenId);

        ehcacheService.getAdminTokenCache().put(tokenId, adminToken);

        //删除使用过的验证码缓存
        ehcacheService.getVerificationCodeCache().evict(HttpTools.getIp());
        return R.ok(adminToken);
    }

    //如果当前登录的账号已经是登陆状态 则删除之前的登录缓存
    private void checkLoginCache(String account) {
        Cache c = (Cache) ehcacheService.getAdminTokenCache().getNativeCache();
        List<String> list = c.getKeys();
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(tokenId -> {
                if (tokenId.contains(String.valueOf(account))) {
                    ehcacheService.getAdminTokenCache().evict(tokenId);
                }
            });
        }
    }

    @PostMapping("/logout")
    @ApiOperation(value = "退出登录", notes = "退出登录")
    public R logout() {
        ehcacheService.getAdminTokenCache().evict(TokenTools.getAdminToken().getAccount());
        return R.ok(null);
    }


}
