package com.ent.happychat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync //开启异步注解功能
@EnableCaching //开启缓存
@EnableScheduling //开启定时任务
@SpringBootApplication
public class HappyChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(HappyChatApplication.class, args);
    }

}
