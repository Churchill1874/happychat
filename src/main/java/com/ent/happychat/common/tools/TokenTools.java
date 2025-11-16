package com.ent.happychat.common.tools;

import cn.hutool.core.util.RandomUtil;
import com.ent.happychat.common.constant.CacheKeyConstant;
import com.ent.happychat.common.exception.TokenException;
import com.ent.happychat.pojo.resp.admin.AdminTokenResp;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.service.EhcacheService;
import com.ent.happychat.service.PlayerHelper;
import com.ent.happychat.service.PlayerTokenService;
import org.apache.commons.lang3.StringUtils;
import org.ehcache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * token工具类
 */
@Component
public class TokenTools {

    //因为static修饰成员变量不支持自动注入 所以以下面方式给静态成员注入
    private static EhcacheService ehcacheService;
    @Autowired
    public void setEhcacheService(EhcacheService ehcacheService) {
        TokenTools.ehcacheService = ehcacheService;
    }


    private static PlayerHelper playerHelper;
    @Autowired
    public void setPlayerHelper(PlayerHelper playerHelper){
        TokenTools.playerHelper = playerHelper;
    }

    /**
     * 获取管理员登录信息
     *
     * @return
     */
    public static AdminTokenResp getAdminToken(boolean needCheck) {
        String headerToken = HttpTools.getHeaderToken();
        if (StringUtils.isBlank(headerToken)) {
            //如果要求在请求头里的token-id不能为空 要校验令牌
            if (needCheck) {
                throw new TokenException();
            } else {
                return null;
            }
        }

        AdminTokenResp adminTokenResp = ehcacheService.adminTokenCache().get(headerToken);
        if (needCheck && adminTokenResp == null) {
            throw new TokenException();
        }
        return adminTokenResp;
    }

    public static String getAdminName() {
        return getAdminToken(true).getName();
    }

    /**
     * 获取管理员登录信息
     *
     * @return
     */
    public static PlayerTokenResp getPlayerToken(boolean needCheck) {
        String headerToken = HttpTools.getHeaderToken();
        if (StringUtils.isBlank(headerToken)) {
            //如果要求在请求头里的token-id不能为空 要校验令牌
            if (needCheck) {
                throw new TokenException();
            } else {
                return null;
            }
        }

        PlayerTokenResp playerTokenResp = ehcacheService.playerTokenCache().get(headerToken);
        if (needCheck && playerTokenResp == null) {

            playerTokenResp = playerHelper.checkAndUpdate(headerToken);
            if (playerTokenResp != null){
                return playerTokenResp;
            } else {
                throw new TokenException();
            }
        }
        return playerTokenResp;
    }

    /**
     * 获取当前在线人数 随机生成
     *
     * @return
     */
    public static int onlineCountRandom() {
        Cache<String, Integer> cache = ehcacheService.playerOnlineCount();
        Integer playerOnlineCount = cache.get(CacheKeyConstant.PLAYER_ONLINE_COUNT);
        if (playerOnlineCount != null) {
            return playerOnlineCount;
        }

        //获取当前几点了
        int currentHour = LocalDateTime.now().getHour();

        //早晨七点以前人最少
        if (currentHour < 7) {
            playerOnlineCount = RandomUtil.randomInt(1, 20);
        }
        //上午人多一点
        if (currentHour >= 7 && currentHour < 11) {
            playerOnlineCount = RandomUtil.randomInt(10, 50);
        }
        //中午下午人再多一点
        if (currentHour >= 11 && currentHour < 17) {
            playerOnlineCount = RandomUtil.randomInt(40, 80);
        }
        //晚上人最多
        if (currentHour >= 17) {
            playerOnlineCount = RandomUtil.randomInt(70, 150);
        }

        cache.put(CacheKeyConstant.PLAYER_ONLINE_COUNT, playerOnlineCount);
        return playerOnlineCount;
    }

}
