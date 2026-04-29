package com.ent.happychat.task;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ent.happychat.common.tools.FreeNewsTools;
import com.ent.happychat.common.tools.PoliticsNewsTools;
import com.ent.happychat.common.tools.TimeUtils;
import com.ent.happychat.entity.Politics;
import com.ent.happychat.entity.Society;
import com.ent.happychat.entity.SoutheastAsia;
import com.ent.happychat.entity.Topic;
import com.ent.happychat.mapper.PoliticsMapper;
import com.ent.happychat.mapper.SocietyMapper;
import com.ent.happychat.mapper.SoutheastAsiaMapper;
import com.ent.happychat.mapper.TopicMapper;
import com.ent.happychat.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

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
    @Autowired
    private PoliticsService politicsService;
    @Autowired
    private PoliticsMapper politicsMapper;
    @Autowired
    private TopicService topicService;
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private SoutheastAsiaService southeastAsiaService;
    @Autowired
    private SoutheastAsiaMapper southeastAsiaMapper;
    @Autowired
    private SocietyService societyService;
    @Autowired
    private SocietyMapper societyMapper;

    /**
     * 每分钟执行定时任务
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void perMinute() {
        log.info("进入定时任务");
        LocalDateTime currentTime = LocalDateTime.now();
        log.info("获取到的时间:{}", currentTime);

        //随即赞
        randomLikes(currentTime);

        //定时检查 满足时间条件就去拉取新闻  暂时注释 不搞国内新闻
        //newsService.pullNews(currentTime, Arrays.asList(JuHeNewsCategoryEnum.values()), true);

        //每天晚上0点删除一遍 上传未使用的文件
        if (currentTime.getHour() == 0 && currentTime.getMinute() == 0) {
            //清理未用的磁盘上的图片
            uploadRecordService.cleanNotUsedFile();
        }

        //早晨五点
        if (currentTime.getHour() == 5 && currentTime.getMinute() == 0) {
            //拉取 freenews
            southeastAsiaService.southeastAsiaPull();
        }

        // 早上6点 抓政治新闻
        if (currentTime.getHour() == 6 && currentTime.getMinute() == 0) {
            //政治新闻 freenews
            politicsService.politicsPull();
        }

        //中午12点
        if (currentTime.getHour() == 12 && currentTime.getMinute() == 0) {

        }

        //下午六点
        if (currentTime.getHour() == 18 && currentTime.getMinute() == 0) {

        }

    }






    private void randomLikes(LocalDateTime currentTime) {
        if (currentTime.getMinute() % 5 == 0) {
            //获取最新50个东南亚新闻 然后根据返回集合长度 作为上限下限,随机点赞
            List<SoutheastAsia> southeastAsiaList = southeastAsiaService.list(new LambdaQueryWrapper<SoutheastAsia>()
                    .orderByDesc(SoutheastAsia::getCreateTime)
                    .last("limit 50"));

            if (CollectionUtils.isNotEmpty(southeastAsiaList)) {
                SoutheastAsia southeastAsia = southeastAsiaList.get(RandomUtil.randomInt(0, southeastAsiaList.size()));
                southeastAsiaMapper.increaseViewsCount(southeastAsia.getId());
            }

            //获取最新50个话题
            List<Topic> topicList = topicService.list(new LambdaQueryWrapper<Topic>()
                    .orderByDesc(Topic::getCreateTime)
                    .last("limit 50"));

            if (CollectionUtils.isNotEmpty(topicList)) {
                Topic topic = topicList.get(RandomUtil.randomInt(0, topicList.size()));
                topicMapper.increaseViewsCount(topic.getId());
            }

            //获取最新50个社会新闻
            List<Society> societyList = societyService.list(new LambdaQueryWrapper<Society>()
                    .orderByDesc(Society::getCreateTime)
                    .last("limit 50"));

            if (CollectionUtils.isNotEmpty(societyList)) {
                Society society = societyList.get(RandomUtil.randomInt(0, societyList.size()));
                societyMapper.increaseViewsCount(society.getId());
            }

            //获取最新10个政治新闻
            List<Politics> politicsList = politicsService.list(new LambdaQueryWrapper<Politics>()
                    .orderByDesc(Politics::getCreateTime)
                    .last("limit 10"));

            if (CollectionUtils.isNotEmpty(politicsList)) {
                Politics politics = politicsList.get(RandomUtil.randomInt(0, politicsList.size()));
                politicsMapper.increaseViewsCount(politics.getId());
            }


        }
    }


}