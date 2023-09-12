package com.ent.happychat.common.aspect;

import com.ent.happychat.common.constant.RoleEnum;
import com.ent.happychat.common.constant.UserStatusEnum;
import com.ent.happychat.common.exception.AuthException;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.pojo.vo.Token;
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
        Token token = TokenTools.getToken();
        if (token.getRole() != RoleEnum.PLAYER.getCode() || token.getStatus() == UserStatusEnum.DISABLE.getValue()) {
            throw new AuthException();
        }
    }

/*    @After("loginCheck()")
    public void afterCut(){

    }*/


}
