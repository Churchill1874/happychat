package com.ent.happychat.common.tools;

import com.ent.happychat.common.constant.enums.CacheTypeEnum;
import com.ent.happychat.common.exception.TokenException;
import com.ent.happychat.pojo.resp.admin.AdminTokenResp;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.service.EhcacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * token工具类
 */
@Component
public class TokenTools {

    private static EhcacheService ehcacheService;

    //因为static修饰成员变量不支持自动注入 所以以下面方式给静态成员注入
    @Autowired
    public void setEhcacheService(EhcacheService ehcacheService) {
        TokenTools.ehcacheService = ehcacheService;
    }

    /**
     * 获取管理员登录信息
     * @return
     */
    public static AdminTokenResp getAdminToken() {
        AdminTokenResp adminTokenResp = ehcacheService.getCache(CacheTypeEnum.ADMIN_TOKEN).get(HttpTools.getHeaderToken(), AdminTokenResp.class);
        if (adminTokenResp == null) {
            throw new TokenException();
        }
        return adminTokenResp;
    }


    /**
     * 获取管理员登录信息
     * @return
     */
    public static PlayerTokenResp getPlayerToken() {
        PlayerTokenResp playerTokenResp = ehcacheService.getCache(CacheTypeEnum.PLAYER_TOKEN).get(HttpTools.getHeaderToken(), PlayerTokenResp.class);
        if (playerTokenResp == null) {
            throw new TokenException();
        }
        return playerTokenResp;
    }


}
