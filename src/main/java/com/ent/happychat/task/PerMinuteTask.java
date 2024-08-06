package com.ent.happychat.task;

import com.ent.happychat.common.constant.enums.JuHeNewsCategoryEnum;
import com.ent.happychat.service.NewsService;
import com.ent.happychat.service.UploadRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 定时任务类
 */
@Slf4j
@Component
public class PerMinuteTask {

    @Autowired
    private NewsService newsService;
    @Autowired
    private UploadRecordService uploadRecordService;

    /**
     * 每分钟执行定时任务
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void perMinute() {
        LocalDateTime currentTime = LocalDateTime.now();
        //定时检查 满足时间条件就去拉取新闻
        newsService.pullNews(currentTime, Arrays.asList(JuHeNewsCategoryEnum.values()), true);

        //每天晚上0点删除一遍 上传未使用的文件
        if (currentTime.getHour() == 0 && currentTime.getMinute() == 0){
            uploadRecordService.cleanNotUsedFile();
        }
    }

}