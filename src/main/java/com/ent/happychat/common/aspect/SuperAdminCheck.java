package com.ent.happychat.common.aspect;

import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.pojo.dto.PlayerToken;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class SuperAdminCheck {

    //定位切面的目标 是一个注解
    @Pointcut("@annotation(com.ent.happychat.common.annotation.SuperAdminLoginCheck)")
    public void superAdminLoginCheck() {

    }

    @Before("superAdminLoginCheck()")
    public void beforeCut(JoinPoint joinPoint) {
        PlayerToken playerToken = TokenTools.getToken();
/*        if (token.getRole() != RoleEnum.SUPER_ADMIN.getCode()) {
            throw new AuthException();
        }*/
    }


}
