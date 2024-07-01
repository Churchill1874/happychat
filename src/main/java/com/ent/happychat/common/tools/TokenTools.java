package com.ent.happychat.common.tools;

import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.exception.TokenException;
import com.ent.happychat.pojo.resp.admin.AdminTokenResp;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.service.EhcacheService;
import org.apache.commons.lang3.StringUtils;
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
    public static AdminTokenResp getAdminToken(boolean needCheck) {
        String headerToken = HttpTools.getHeaderToken();
        if (StringUtils.isBlank(headerToken)){
            //如果要求在请求头里的token_id不能为空 要校验令牌
            if (needCheck){
                throw new TokenException();
            } else {
                return null;
            }
        }

        AdminTokenResp adminTokenResp = ehcacheService.adminTokenCache().get(headerToken);
        if (adminTokenResp == null) {
            throw new TokenException();
        }
        return adminTokenResp;
    }


    /**
     * 获取管理员登录信息
     * @return
     */
    public static PlayerTokenResp getPlayerToken(boolean needCheck) {
        String headerToken = HttpTools.getHeaderToken();
        if (StringUtils.isBlank(headerToken)){
            //如果要求在请求头里的token_id不能为空 要校验令牌
            if (needCheck){
                throw new TokenException();
            } else {
                return null;
            }
        }

        PlayerTokenResp playerTokenResp = ehcacheService.playerTokenCache().get(HttpTools.getHeaderToken());
        if (playerTokenResp == null) {
            throw new TokenException();
        }
        return playerTokenResp;
    }


}
