package com.ent.happychat.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务类
 */
@Slf4j
@Component
public class PerMinuteTask {



    /**
     * 每分钟执行定时任务
     */
    @Scheduled(cron = "0 */1 * * * ?")
    private void perMinute() {

    }


}