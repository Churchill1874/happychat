package com.ent.happychat.service.serviceimpl;

import com.ent.happychat.common.constant.enums.CacheTypeEnum;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.exception.IpException;
import com.ent.happychat.common.tools.GenerateTools;
import com.ent.happychat.common.tools.HttpTools;
import com.ent.happychat.entity.Blacklist;
import com.ent.happychat.service.BlacklistService;
import com.ent.happychat.service.EhcacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 该类对ehcache.xml配置文件里面已经配置的缓存容器进行实现获取，方便使用
 */
@Slf4j
@Service
public class EhcacheServiceImpl implements EhcacheService {

    @Autowired
    private BlacklistService blacklistService;

    @Autowired
    private CacheManager cacheManager;


    private final String BLACKLIST_SET_KEY = "blacklist_set_key";

    @Override
    public Cache getCache(CacheTypeEnum cacheTypeEnum) {
        if (CacheTypeEnum.PLAYER_TOKEN == cacheTypeEnum) {
            return cacheManager.getCache("player_token");
        }
        if (CacheTypeEnum.ADMIN_TOKEN == cacheTypeEnum) {
            return cacheManager.getCache("admin_token");
        }
        if (CacheTypeEnum.LOCK_3_SECOND == cacheTypeEnum) {
            return cacheManager.getCache("lock_3_Second");
        }
        if (CacheTypeEnum.VERIFICATION_CODE == cacheTypeEnum) {
            return cacheManager.getCache("verification_code");
        }
        if (CacheTypeEnum.BLACKLIST == cacheTypeEnum) {
            return cacheManager.getCache("blacklist");
        }
        return null;
    }

    @Override
    public String getString(CacheTypeEnum cacheTypeEnum, String key) {
        Cache cache = getCache(cacheTypeEnum);
        if (cache == null) {
            return null;
        }
        return cache.get(key, String.class);
    }

    @Override
    public void checkIp3SecondsClick(Integer limitCount, String remarks) {
        String ip = HttpTools.getIp();


        Cache cache = this.getCache(CacheTypeEnum.LOCK_3_SECOND);
        Integer reqCount = cache.get(ip, Integer.class);


        if (reqCount != null) {


            if (reqCount >= limitCount) {
                //如果ip存在黑名单就更新时间
                Blacklist blacklist = blacklistService.findByIp(ip);
                if (blacklist != null) {
                    blacklist.setUpdateTime(LocalDateTime.now());
                    blacklist.setUpdateName("系统");
                    blacklistService.updateById(blacklist);
                } else { //否则就新增
                    blacklist = new Blacklist();
                    blacklist.setIp(ip);
                    blacklist.setRemarks(remarks);
                    blacklist.setCreateName("系统");
                    blacklistService.insert(blacklist);
                }
                throw new IpException();
            } else {
                cache.put(ip, reqCount + 1);
            }


        } else {
            cache.put(ip, 1);
        }
    }


    @Override
    public String getVC(String key, Integer limitCount, String remarks) {
        //添加频繁点击校验 3秒内点击超过30次 检查警告日志 如果该ip已经存在警告则拉黑 不存在则新加警告日志
        this.checkIp3SecondsClick(limitCount, remarks);

        //获取验证码
        String codeImageStream = null;
        String code = null;
        try {
            code = GenerateTools.getCaptchaText(5);
            codeImageStream = GenerateTools.getCaptchaImage(code);
        } catch (IOException e) {
            log.error("生成验证码异常:{}", e.getMessage());
            throw new DataException(e.getMessage());
        }

        this.getCache(CacheTypeEnum.VERIFICATION_CODE).put(key, code);

        return codeImageStream;
    }

    @Override
    public Set<String> getBlacklistIpSetCache() {
        Cache cache = getCache(CacheTypeEnum.BLACKLIST);
        return cache.get(BLACKLIST_SET_KEY, Set.class);
    }


    @Override
    public void setBlacklistIpSetCache(Set<String> blacklistIpSet) {
        Cache cache = getCache(CacheTypeEnum.BLACKLIST);
        cache.put(BLACKLIST_SET_KEY, blacklistIpSet);
    }

}
