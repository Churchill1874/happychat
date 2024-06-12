package com.ent.happychat.common.aspect;

import com.ent.happychat.common.exception.IpException;
import com.ent.happychat.common.tools.HttpTools;
import com.ent.happychat.service.BlacklistService;
import com.ent.happychat.service.EhcacheService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Aspect
@Component
public class BlacklistAspect {

    @Autowired
    private EhcacheService ehcacheService;

    @Autowired
    private BlacklistService blacklistService;

    @Pointcut("execution(* com.ent.happychat.controller.manage.*.*(..))")
    public void blacklistPointCut() {
    }

    @Before("blacklistPointCut()")
    public void beforeExecute() {
        String ip = HttpTools.getIp();
        Set<String> blacklistIpSet = blacklistService.getIpSet();
        if (blacklistIpSet.contains(ip)) {
            log.error("黑名单ip访问异常:{}", ip);
            throw new IpException(ip);
        }
    }

}
