package com.ent.happychat.service;

import com.ent.happychat.common.constant.enums.CacheTypeEnum;
import org.springframework.cache.Cache;

import java.util.Set;

/**
 * 缓存服务
 */
public interface EhcacheService {

    /**
     * 根据类型获取缓存
     * @param cacheTypeEnum
     * @return
     */
    Cache getCache(CacheTypeEnum cacheTypeEnum);

    /**
     * 根据类型和key获取缓存
     * @param cacheTypeEnum
     * @param key
     * @return
     */
    String getString(CacheTypeEnum cacheTypeEnum, String key);

    /**
     * 获取验证码 并设置每3秒的限制请求次数 和提示语
     * @param limitCount
     * @param remarks
     * @return
     */
    String getVC(String key, Integer limitCount, String remarks);

    /**
     * 校验ip 3秒内频繁点击超过指定次数
     *
     * @param limitCount
     * @return
     */
    void checkIp3SecondsClick(Integer limitCount, String remarks);

    /**
     * 获取黑名单ip集合set
     * @return
     */
    Set<String> getBlacklistIpSetCache();

    /**
     * 更新设置黑名单ip集合set
     * @return
     */
    void setBlacklistIpSetCache(Set<String> blacklistIpSet);

}
