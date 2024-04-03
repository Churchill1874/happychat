package com.ent.happychat.service.serviceimpl;

import com.ent.happychat.common.tools.HttpTools;
import com.ent.happychat.entity.Blacklist;
import com.ent.happychat.service.BlacklistService;
import com.ent.happychat.service.EhcacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * 该类对ehcache.xml配置文件里面已经配置的缓存容器进行实现获取，方便使用
 */
@Service
public class EhcacheServiceImpl implements EhcacheService {

    @Autowired
    private BlacklistService blacklistService;

    @Autowired
    private CacheManager cacheManager;

    @Override
    public Cache getPlayerTokenCache() {
        return cacheManager.getCache("playerToken");
    }

    @Override
    public Cache getAdminTokenCache() {
        return cacheManager.getCache("adminToken");
    }

    @Override
    public Cache get3SecondLockCache() {
        return cacheManager.getCache("lock3Second");
    }

    @Override
    public Cache getVerificationCodeCache() {
        return cacheManager.getCache("verificationCode");
    }

    @Override
    public Cache getSmsVerificationCodeCache() {
        return cacheManager.getCache("smsVerificationCode");
    }

    @Override
    public Cache getBlacklistCache() {
        return cacheManager.getCache("blacklist");
    }

    @Override
    public Cache getFootballMatchCache() {
        return cacheManager.getCache("footballMatch");
    }

    @Override
    public boolean checkIp3SecondsClick(Integer limitCount, String remarks) {
        String ip = HttpTools.getIp();
        Cache cache = this.get3SecondLockCache();
        Integer reqCount = cache.get(ip, Integer.class);
        if (reqCount != null) {
            if (reqCount >= limitCount) {
                Blacklist blacklist = new Blacklist();
                blacklist.setIp(ip);
                blacklist.setRemarks(remarks);
                blacklistService.insert(blacklist);
                return true;
            } else {
                cache.put(ip, reqCount + 1);
            }
        } else {
            cache.put(ip, 1);
        }
        return false;
    }


}
