package com.ent.happychat.task;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ent.happychat.common.constant.CacheKeyConstant;
import com.ent.happychat.common.constant.enums.NewsCategoryEnum;
import com.ent.happychat.common.tools.api.NewsTools;
import com.ent.happychat.entity.News;
import com.ent.happychat.mapper.NewsMapper;
import com.ent.happychat.pojo.resp.news.HomeNews;
import com.ent.happychat.service.EhcacheService;
import com.ent.happychat.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 定时任务类
 */
@Slf4j
@Component
public class PerMinuteTask {

    @Autowired
    private NewsService newsService;
    @Autowired
    private NewsMapper newsMapper;
    @Autowired
    private EhcacheService ehcacheService;

    /**
     * 每分钟执行定时任务
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void perMinute() {
        LocalDateTime currentTime = LocalDateTime.now();

        //执行新闻定时任务
        reqNewsApiTask(currentTime);


    }


    //请求新闻定时任务
    @Async
    public void reqNewsApiTask(LocalDateTime currentTime) {
        int hour = currentTime.getHour();
        int minutes = currentTime.getMinute();
        List<News> newsList = new ArrayList<>();

        //只保留2个月的新闻数据 超过的删除
        if (hour == 0 && minutes ==0){
            newsService.clean2MonthsAgo(currentTime);
        }

        //6点 到晚上 23点 之间 每小时获取一次 军事,头条,新闻,体育,娱乐新闻
        if (hour >= 6) {
            //0分时候请求
            if (minutes == 0) {
                //头条
                List<News> headlinesNews = NewsTools.getNewsData(NewsCategoryEnum.HEADLINES, 5);
                newsList.addAll(headlinesNews);

                //新闻
                List<News> sportsNews = NewsTools.getNewsData(NewsCategoryEnum.SPORTS, 11);
                newsList.addAll(sportsNews);

                //体育
                List<News> news = NewsTools.getNewsData(NewsCategoryEnum.NEWS, 5);
                newsList.addAll(news);

                //军事
                List<News> militaryAffairsNews = NewsTools.getNewsData(NewsCategoryEnum.MILITARY_AFFAIRS, 5);
                newsList.addAll(militaryAffairsNews);

                //科技
                List<News> scienceNews = NewsTools.getNewsData(NewsCategoryEnum.SCIENCE, 2);
                newsList.addAll(scienceNews);

                //娱乐
                List<News> entertainmentNews = NewsTools.getNewsData(NewsCategoryEnum.ENTERTAINMENT, 1);
                newsList.addAll(entertainmentNews);

                //女性
                List<News> womenNews = NewsTools.getNewsData(NewsCategoryEnum.WOMAN, 1);
                newsList.addAll(womenNews);
            }
        }

        if (CollectionUtils.isNotEmpty(newsList)) {
            log.info("执行新闻定时任务,处理新闻数据{}条", newsList.size());

            //遍历过滤掉三方返回的html标签 ,并且抽取图片路径拼在一起保存
            for(News news: newsList){
                Document document = Jsoup.parse(news.getContent());
                news.setFilterContent(document.text());

                //获取三方返回的新闻内容里面的图片路径
                Elements images = document.select("img");
                //将所有图片拼在一起用逗号隔开
                List<String> imagePaths = images.stream()
                        .map(img -> img.attr("src"))
                        .collect(Collectors.toList());

                String contentImagePath = String.join(",", imagePaths);

                news.setContentImagePath(contentImagePath);
            }

            newsMapper.insertBatchIgnore(newsList);

            //清理首页的新闻列表缓存
            ehcacheService.homeNewsCache().remove(CacheKeyConstant.HOME_NEWS_KEY);
        }
    }

}