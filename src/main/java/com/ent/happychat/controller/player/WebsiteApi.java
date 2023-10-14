package com.ent.happychat.controller.player;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.constant.enums.LogTypeEnum;
import com.ent.happychat.common.constant.enums.UserStatusEnum;
import com.ent.happychat.common.exception.AccountOrPasswordException;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.tools.CodeTools;
import com.ent.happychat.common.tools.GenerateTools;
import com.ent.happychat.common.tools.HttpTools;
import com.ent.happychat.entity.Blacklist;
import com.ent.happychat.entity.LogRecord;
import com.ent.happychat.entity.User;
import com.ent.happychat.pojo.req.website.LoginPlayerReq;
import com.ent.happychat.pojo.req.website.RegisterReq;
import com.ent.happychat.pojo.vo.Token;
import com.ent.happychat.service.BlacklistService;
import com.ent.happychat.service.EhcacheService;
import com.ent.happychat.service.LogRecordService;
import com.ent.happychat.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UserService userService;
    @Autowired
    private LogRecordService logRecordService;
    @Autowired
    private BlacklistService blacklistService;
    @PostMapping("/register")
    @ApiOperation(value = "注册")
    public synchronized R<User> register(@RequestBody @Valid RegisterReq req) {
        //校验图形验证码
        //checkGraphicVerificationCode(req.getVerificationCode());

        User user = userService.findByName(req.getName());
        if (user != null) {
            return R.failed("该用户名已被注册");
        }

        user = userService.findByAccount(req.getAccount());
        if (user != null) {
            return R.failed("该账号已经存在");
        }

        //todo 不需要先临时注释
        //校验短信验证码
        //checkSmsVerificationCode(req.getPhoneNumber(), req.getSmsVerificationCode());

        //校验ip总注册数量是否超过10个
        //checkRegister(10);

        //创建用户数据
        user = new User();
        BeanUtils.copyProperties(req, user);

        String passwordReq = CodeTools.md5AndSalt(req.getPassword());
        user.setAccount(req.getAccount());
        user.setPassword(passwordReq);
        user.setBalance(BigDecimal.ZERO);
        user.setLevel(1);
        user.setName(req.getName());
        user.setAddress(HttpTools.getAddress());
        user.setStatus(UserStatusEnum.NORMAL);
        user.setCreateTime(LocalDateTime.now());
        userService.add(user);

        //记录登录日志
        //logRecordService.insert(GenerateTools.registerLog(user.getName(), user.getAccount()));

        return R.ok(null);
    }

    @PostMapping("/login")
    @ApiOperation(value = "登录", notes = "手机号或账号 至少有一个有值")
    public R<String> login(@RequestBody @Valid LoginPlayerReq req) {
        //校验请求参数
        if (req.getAccount() == null) {
            throw new DataException("网名或账号不能同时为空");
        }

        //校验图形验证码
        //checkGraphicVerificationCode(req.getVerificationCode());

        //判断账号密码是否正确
        User user = null;
        if (req.getAccount() != null) {
            user = userService.findByAccount(req.getAccount());
        }

        if (user == null) {
            throw new AccountOrPasswordException();
        }

        //对比登录密码和正确密码
        String password = user.getPassword();
        String passwordReq = CodeTools.md5AndSalt(req.getPassword());

        //如果填写的登录密码是错误的
        if (!password.equals(passwordReq)) {
            throw new AccountOrPasswordException();
        }


        //如果已经登陆过,删除之前的tokenId和缓存
        this.checkLoginCache(user.getAccount());

        //生成token并返回
        Token token = GenerateTools.createToken(user);
        String tokenId = GenerateTools.createTokenId(user.getAccount());
        ehcacheService.getTokenCache().put(tokenId, token);

        //删除使用过的验证码缓存
        ehcacheService.getVerificationCodeCache().evict(HttpTools.getIp());
        return R.ok(tokenId);
    }


    //如果当前登录的账号已经是登陆状态 则删除之前的登录缓存
    private void checkLoginCache(String account) {
        Cache c = (Cache) ehcacheService.getTokenCache().getNativeCache();
        List<String> list = c.getKeys();
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(tokenId -> {
                if (tokenId.contains(String.valueOf(account))) {
                    ehcacheService.getTokenCache().evict(tokenId);
                }
            });
        }
    }

    /**
     * 校验ip注册次数限制
     */
    private void checkRegister(int limitCount) {
        String ip = HttpTools.getIp();
        List<LogRecord> registerLogListRecord = logRecordService.registerList(ip);
        //校验ip是否已经注册了10个以上的账号
        if (CollectionUtils.isNotEmpty(registerLogListRecord)) {
            int count = registerLogListRecord.size();
            if (count >= limitCount) {
                final String warnContent = "注册次数过限";
                logRecordService.insert(GenerateTools.createWarnLog(warnContent, ip, HttpTools.getPlatform()));

                //校验注册次数过限警告次数,是否大于等于20次,如果大于将ip拉黑
                LogRecord logRecord = new LogRecord();
                logRecord.setType(LogTypeEnum.WARN);
                logRecord.setIp(ip);
                logRecord.setMessage(warnContent);
                List<LogRecord> registerWarnLogListRecord = logRecordService.getList(logRecord);

                if (CollectionUtils.isNotEmpty(registerWarnLogListRecord) && registerWarnLogListRecord.size() > 20) {
                    Blacklist blacklist = new Blacklist();
                    blacklist.setIp(ip);
                    blacklist.setRemarks("大量注册账号");
                    blacklistService.insert(blacklist);
                    throw new DataException("注册次数已达上限,若仍需注册请联系管理员");
                }

            }
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
