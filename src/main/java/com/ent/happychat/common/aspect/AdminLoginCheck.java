package com.ent.happychat.common.aspect;

import com.ent.happychat.common.constant.enums.ManageRoleEnum;
import com.ent.happychat.common.constant.enums.UserStatusEnum;
import com.ent.happychat.common.exception.AuthException;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.pojo.resp.admin.AdminTokenResp;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class AdminLoginCheck {

    //定位切面的目标 是一个注解
    @Pointcut("@annotation(com.ent.happychat.common.annotation.AdminLoginCheck)")
    public void adminLoginCheck() {

    }

    @Before("adminLoginCheck()")
    public void beforeCut(JoinPoint joinPoint) {
        TokenTools.getAdminToken(true);
    }

/*    @After("loginCheck()")
    public void afterCut(){

    }*/


}
