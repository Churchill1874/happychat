package com.ent.happychat.controller.manage;

import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.tools.GenerateTools;
import com.ent.happychat.common.tools.HttpTools;
import com.ent.happychat.service.EhcacheService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(tags = "验证码")
@RequestMapping("/manage/verificationCode")
public class VerificationCodeController {

    @Autowired
    private EhcacheService ehcacheService;


    @PostMapping("/get")
    @ApiOperation(value = "获取验证码", notes = "获取验证码")
    public synchronized R<String> get() {
        return R.ok(ehcacheService.getVC(HttpTools.getIp(),30,"每3秒超过30次点击验证码"));
    }

}
