package com.ent.happychat.common.aspect;

import com.ent.happychat.common.constant.CacheKeyConstant;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.pojo.resp.news.HomeResp;
import com.ent.happychat.service.EhcacheService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.ehcache.Cache;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Aspect
@Component
public class HomeDataClean {

    @Resource
    private EhcacheService ehcacheService;

    //定位切面的目标 是一个注解
    @Pointcut("@annotation(com.ent.happychat.common.annotation.HomeDataClean)")
    public void homeDataClean() {

    }

    @Before("homeDataClean()")
    public void beforeCut(JoinPoint joinPoint) {
        Cache<String, HomeResp> cache = ehcacheService.homeCache();
        cache.remove(CacheKeyConstant.HOME_DATA);
    }

/*    @After("loginCheck()")
    public void afterCut(){

    }*/


}
