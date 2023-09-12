package com.ent.happychat.config;

import com.ent.happychat.common.constant.RoleEnum;
import com.ent.happychat.common.tools.CodeTools;
import com.ent.happychat.entity.User;
import com.ent.happychat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class InitConfig {

    //超管管理员账号
    private static final int SUPER_ADMIN_ACCOUNT = 5000;

    private static final String PASSWORD = "111111a";
    @Autowired
    private UserService userService;

    //获取创建机器人开关
    @Value("${init.create.bot}")
    private boolean createBot;

    /**
     * 项目启动时运行方法
     */
    @PostConstruct
    private void run(){
      log.info("获取创建机器人开关配置:{}",createBot);

      User superAdmin = userService.findByAccount(SUPER_ADMIN_ACCOUNT);
      if (superAdmin == null){
          User user = new User();
          user.setName("超级管理员");
          user.setAccount(SUPER_ADMIN_ACCOUNT);
          user.setPassword(CodeTools.md5AndSalt(PASSWORD));
          user.setAvatar(10);
          user.setRole(RoleEnum.SUPER_ADMIN.getCode());
          user.setPlatform(0);
          userService.add(user);
          log.info("成功创建超级管理员账号");
      }

    }

}
