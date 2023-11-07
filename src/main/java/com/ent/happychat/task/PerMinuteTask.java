package com.ent.happychat.task;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ent.happychat.common.constant.enums.NewsCategoryEnum;
import com.ent.happychat.common.tools.api.NewsTools;
import com.ent.happychat.entity.News;
import com.ent.happychat.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        int hour = currentTime.getHour();
        int minutes = currentTime.getMinute();

        //执行新闻定时任务
        reqNewsApiTask(hour, minutes);


    }


    //请求新闻定时任务
    @Async
    public void reqNewsApiTask(int hour, int minutes) {
        List<News> newsList = new ArrayList<>();
        //12点 请求育儿新闻
        if (12 == hour && minutes == 0) {
            List<News> parentingNews = NewsTools.getNewsData(NewsCategoryEnum.PARENTING, 30);
            newsList.addAll(parentingNews);
        }

        //8点 和 18点 请求女性新闻 和科技新闻
        if ((8 == hour && minutes == 0) || (18 == hour && minutes == 0)) {
            List<News> womanNews = NewsTools.getNewsData(NewsCategoryEnum.WOMAN, 30);
            newsList.addAll(womanNews);

            List<News> scienceNews = NewsTools.getNewsData(NewsCategoryEnum.SCIENCE, 30);
            newsList.addAll(scienceNews);
        }

        //6点 到晚上 23点 之间 每小时获取一次 军事,头条,新闻,体育,娱乐新闻
        if (hour >= 6 && hour < 24) {
            //0分时候请求
            if (minutes == 0) {
                List<News> headlinesNews = NewsTools.getNewsData(NewsCategoryEnum.HEADLINES, 30);
                newsList.addAll(headlinesNews);

                List<News> news = NewsTools.getNewsData(NewsCategoryEnum.NEWS, 30);
                newsList.addAll(news);

                List<News> sportsNews = NewsTools.getNewsData(NewsCategoryEnum.SPORTS, 30);
                newsList.addAll(sportsNews);
            }

            //30分的时候请求
            if (minutes == 30) {
                List<News> militaryAffairsNews = NewsTools.getNewsData(NewsCategoryEnum.MILITARY_AFFAIRS, 30);
                newsList.addAll(militaryAffairsNews);
            }

            //7,11,17,21点请求娱乐新闻
            if ((hour == 7 || hour == 11 || hour == 17 || hour == 19 || hour == 21) && minutes == 0) {
                List<News> entertainmentNews = NewsTools.getNewsData(NewsCategoryEnum.ENTERTAINMENT, 30);
                newsList.addAll(entertainmentNews);
            }
        }

        if (CollectionUtils.isNotEmpty(newsList)) {
            log.info("执行新闻定时任务,处理新闻数据{}条", newsList.size());
            newsService.saveList(newsList);
        }
    }

}