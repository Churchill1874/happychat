package com.ent.happychat.service;

import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.pojo.resp.admin.AdminTokenResp;
import com.ent.happychat.pojo.resp.news.HomeNews;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import org.ehcache.Cache;

import java.util.Set;

/**
 * 缓存服务
 */
public interface EhcacheService {

    /**
     * 获取3秒锁缓存容器
     * @return
     */
    Cache<String, Integer> lock3SecondCache();

    /**
     * 获取验证码缓存容器
     * @return
     */
    Cache<String, String> verificationCache();


    /**
     * 获取管理员登录token缓存容器
     * @return
     */
    Cache<String, AdminTokenResp> adminTokenCache();

    /**
     * 获取玩家token缓存容器
     * @return
     */
    Cache<String, PlayerTokenResp> playerTokenCache();

    /**
     * 玩家信息缓存
     * @return
     */
    Cache<String, PlayerInfo> playerInfoCache();

    /**
     * 获取真实在线人数缓存容器
     * @return
     */
    Cache<String, PlayerTokenResp> onlineCountCache();

    /**
     * 获取随机在线人数缓存
     * @return
     */
    Cache<String, Integer> playerOnlineCount();

    /**
     * 首页新闻
     * @return
     */
    Cache<String, HomeNews> homeNewsCache();

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
