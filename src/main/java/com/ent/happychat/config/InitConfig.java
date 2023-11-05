package com.ent.happychat.config;

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

    //获取创建机器人开关
    @Value("${init.create.bot}")
    private boolean createBot;

    /**
     * 项目启动时运行方法
     */
    @PostConstruct
    private void run() {
        log.info("获取创建机器人开关配置:{}", createBot);


    }

}
