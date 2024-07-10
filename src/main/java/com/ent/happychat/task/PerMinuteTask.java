package com.ent.happychat.task;

import com.ent.happychat.common.constant.enums.JuHeNewsCategoryEnum;
import com.ent.happychat.service.NewsService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 定时任务类
 */
@Slf4j
@Component
public class PerMinuteTask {

    @Autowired
    private NewsService newsService;

    /**
     * 每分钟执行定时任务
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void perMinute() {
        LocalDateTime currentTime = LocalDateTime.now();
        newsService.pullNews(currentTime, Lists.newArrayList(JuHeNewsCategoryEnum.values()), true);
    }

}