package com.ent.happychat.service;

import org.springframework.cache.Cache;

/**
 * 缓存服务
 */
public interface EhcacheService {

    /**
     * 获取token缓存容器
     *
     * @return
     */
    Cache getTokenCache();

    /**
     * 获取3秒锁缓存容器
     *
     * @return
     */
    Cache get3SecondLockCache();

    /**
     * 获取验证码缓存容器
     *
     * @return
     */
    Cache getVerificationCodeCache();

    /**
     * 获取短信缓存容器
     *
     * @return
     */
    Cache getSmsVerificationCodeCache();

    /**
     * 黑名单缓存容器
     *
     * @return
     */
    Cache getBlacklistCache();

    /**
     * 获取足球比赛缓存
     *
     * @return
     */
    Cache getFootballMatchCache();

    /**
     * 校验ip 3秒内频繁点击超过指定次数
     *
     * @param limitCount
     * @return
     */
    boolean checkIp3SecondsClick(Integer limitCount, String remarks);

}
