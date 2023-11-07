package com.ent.happychat.common.tools;

import com.ent.happychat.common.exception.TokenException;
import com.ent.happychat.pojo.dto.PlayerToken;
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
     * 获取用户token
     *
     * @return
     */
    public static PlayerToken getToken() {
        PlayerToken playerToken = ehcacheService.getTokenCache().get(HttpTools.getHeaderToken(), PlayerToken.class);
        if (playerToken == null) {
            throw new TokenException();
        }
        return playerToken;
    }

    /**
     * 获取账号可以为空
     * @return
     */
    public static String getAccountMayNull(){
        PlayerToken playerToken = ehcacheService.getTokenCache().get(HttpTools.getHeaderToken(), PlayerToken.class);
        if (playerToken != null){
            return playerToken.getAccount();
        }
        return null;
    }

}
