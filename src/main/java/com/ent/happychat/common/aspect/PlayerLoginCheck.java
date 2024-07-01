package com.ent.happychat.common.aspect;

import com.ent.happychat.common.constant.enums.UserStatusEnum;
import com.ent.happychat.common.exception.AuthException;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class PlayerLoginCheck {

    //定位切面的目标 是一个注解
    @Pointcut("@annotation(com.ent.happychat.common.annotation.PlayerLoginCheck)")
    public void playerLoginCheck() {

    }

    @Before("playerLoginCheck()")
    public void beforeCut(JoinPoint joinPoint) {
        PlayerTokenResp playerToken = TokenTools.getPlayerToken(true);
        if (playerToken.getStatus() == null || playerToken.getStatus() == UserStatusEnum.DISABLE) {
            throw new AuthException();
        }
    }

/*    @After("loginCheck()")
    public void afterCut(){

    }*/


}
