package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.enums.ViewsEnum;
import com.ent.happychat.common.tools.TimeUtils;
import com.ent.happychat.entity.ViewsRecord;
import com.ent.happychat.mapper.ViewsRecordMapper;
import com.ent.happychat.pojo.req.views.ViewsRecordPageReq;
import com.ent.happychat.pojo.resp.report.ViewInfoResp;
import com.ent.happychat.pojo.resp.report.ViewReportResp;
import com.ent.happychat.service.ViewsRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ViewsRecordServiceImpl extends ServiceImpl<ViewsRecordMapper, ViewsRecord> implements ViewsRecordService {

    @Override
    public IPage<ViewsRecord> queryPage(ViewsRecordPageReq po) {
        IPage<ViewsRecord> iPage = new Page<>(po.getPageNum(), po.getPageSize());
        QueryWrapper<ViewsRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(po.getPlayerId() != null, ViewsRecord::getPlayerId, po.getPlayerId())
                .eq(po.getViewsId() != null, ViewsRecord::getViewsId, po.getViewsId())
                .eq(po.getViewsType() != null, ViewsRecord::getViewsType, po.getViewsType())
                .eq(po.getContent() != null, ViewsRecord::getContent, po.getContent())
                .eq(StringUtils.isNotBlank(po.getIp()), ViewsRecord::getIp, po.getIp())
                .orderByDesc(ViewsRecord::getCreateTime);
        return page(iPage, queryWrapper);
    }

    @Override
    public void addViewsRecord(String ip, Long viewsId, String content, Long playerId, String playerName, ViewsEnum viewsType) {
        // 插入浏览记录
        ViewsRecord viewsRecord = new ViewsRecord();
        viewsRecord.setPlayerId(playerId);
        viewsRecord.setViewsId(viewsId);
        viewsRecord.setViewsType(viewsType);
        viewsRecord.setContent(content);
        viewsRecord.setCreateTime(LocalDateTime.now());
        viewsRecord.setCreateName(playerName);
        viewsRecord.setIp(ip);
        save(viewsRecord);
    }

    @Override
    public ViewReportResp viewReport() {
        LambdaQueryWrapper<ViewsRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .ge(ViewsRecord::getCreateTime, TimeUtils.startOfLastMonth())
                .select(ViewsRecord::getId, ViewsRecord::getCreateTime, ViewsRecord::getViewsType);
        List<ViewsRecord> list = list(queryWrapper);

        if (CollectionUtils.isEmpty(list)) {
            return new ViewReportResp();
        }

        ViewReportResp viewReportResp = new ViewReportResp();
        ViewInfoResp southeastView = new ViewInfoResp();
        ViewInfoResp newsView = new ViewInfoResp();
        ViewInfoResp politicsView = new ViewInfoResp();
        ViewInfoResp societyView = new ViewInfoResp();
        ViewInfoResp topicViews = new ViewInfoResp();

        for (ViewsRecord viewsRecord : list) {
            //上个月统计
            if (viewsRecord.getCreateTime().isBefore(TimeUtils.startOfThisMonth())) {
                if(viewsRecord.getViewsType() == ViewsEnum.SOUTHEAST_ASIA){
                    southeastView.setLastMonthView(southeastView.getLastMonthView() + 1);
                }
                if(viewsRecord.getViewsType() == ViewsEnum.NEWS){
                    newsView.setLastMonthView(newsView.getLastMonthView() + 1);
                }
                if(viewsRecord.getViewsType() == ViewsEnum.POLITICS){
                    politicsView.setLastMonthView(politicsView.getLastMonthView() + 1);
                }
                if(viewsRecord.getViewsType() == ViewsEnum.SOCIETY){
                    societyView.setLastMonthView(societyView.getLastMonthView() + 1);
                }
                if(viewsRecord.getViewsType() == ViewsEnum.TOPIC){
                    topicViews.setLastMonthView(topicViews.getLastMonthView() + 1);
                }
            }
            //本月统计
            if (viewsRecord.getCreateTime().isAfter(TimeUtils.endOfLastMonth())) {
                if(viewsRecord.getViewsType() == ViewsEnum.SOUTHEAST_ASIA){
                    southeastView.setThisMonthView(southeastView.getThisMonthView() + 1);
                }
                if(viewsRecord.getViewsType() == ViewsEnum.NEWS){
                    newsView.setThisMonthView(newsView.getThisMonthView() + 1);
                }
                if(viewsRecord.getViewsType() == ViewsEnum.POLITICS){
                    politicsView.setThisMonthView(politicsView.getThisMonthView() + 1);
                }
                if(viewsRecord.getViewsType() == ViewsEnum.SOCIETY){
                    societyView.setThisMonthView(societyView.getThisMonthView() + 1);
                }
                if(viewsRecord.getViewsType() == ViewsEnum.TOPIC){
                    topicViews.setThisMonthView(topicViews.getThisMonthView() + 1);
                }
            }
            //本周统计
            if (viewsRecord.getCreateTime().isAfter(TimeUtils.endOfLastWeek())) {
                if(viewsRecord.getViewsType() == ViewsEnum.SOUTHEAST_ASIA){
                    southeastView.setThisWeekView(southeastView.getThisWeekView() + 1);
                }
                if(viewsRecord.getViewsType() == ViewsEnum.NEWS){
                    newsView.setThisWeekView(newsView.getThisWeekView() + 1);
                }
                if(viewsRecord.getViewsType() == ViewsEnum.POLITICS){
                    politicsView.setThisWeekView(politicsView.getThisWeekView() + 1);
                }
                if(viewsRecord.getViewsType() == ViewsEnum.SOCIETY){
                    societyView.setThisWeekView(societyView.getThisWeekView() + 1);
                }
                if(viewsRecord.getViewsType() == ViewsEnum.TOPIC){
                    topicViews.setThisWeekView(topicViews.getThisWeekView() + 1);
                }
            }
            //上周统计
            if (!viewsRecord.getCreateTime().isBefore(TimeUtils.startOfLastWeek()) && viewsRecord.getCreateTime().isBefore(TimeUtils.endOfThisWeek())) {
                if(viewsRecord.getViewsType() == ViewsEnum.SOUTHEAST_ASIA){
                    southeastView.setLastWeekView(southeastView.getLastWeekView() + 1);
                }
                if(viewsRecord.getViewsType() == ViewsEnum.NEWS){
                    newsView.setLastWeekView(newsView.getLastWeekView() + 1);
                }
                if(viewsRecord.getViewsType() == ViewsEnum.POLITICS){
                    politicsView.setLastWeekView(politicsView.getLastWeekView() + 1);
                }
                if(viewsRecord.getViewsType() == ViewsEnum.SOCIETY){
                    societyView.setLastWeekView(societyView.getLastWeekView() + 1);
                }
                if(viewsRecord.getViewsType() == ViewsEnum.TOPIC){
                    topicViews.setLastWeekView(topicViews.getLastWeekView() + 1);
                }
            }
            //今日统计
            if (!viewsRecord.getCreateTime().isBefore(TimeUtils.startOfToday())) {
                if(viewsRecord.getViewsType() == ViewsEnum.SOUTHEAST_ASIA){
                    southeastView.setTodayView(southeastView.getTodayView() + 1);
                }
                if(viewsRecord.getViewsType() == ViewsEnum.NEWS){
                    newsView.setTodayView(newsView.getTodayView() + 1);
                }
                if(viewsRecord.getViewsType() == ViewsEnum.POLITICS){
                    politicsView.setTodayView(politicsView.getTodayView() + 1);
                }
                if(viewsRecord.getViewsType() == ViewsEnum.SOCIETY){
                    societyView.setTodayView(societyView.getTodayView() + 1);
                }
                if(viewsRecord.getViewsType() == ViewsEnum.TOPIC){
                    topicViews.setTodayView(topicViews.getTodayView() + 1);
                }
            }
            //昨日统计
            if (!viewsRecord.getCreateTime().isBefore(TimeUtils.startOfYesterday()) && viewsRecord.getCreateTime().isBefore(TimeUtils.endOfYesterday())) {
                if(viewsRecord.getViewsType() == ViewsEnum.SOUTHEAST_ASIA){
                    southeastView.setYesterdayView(southeastView.getYesterdayView() + 1);
                }
                if(viewsRecord.getViewsType() == ViewsEnum.NEWS){
                    newsView.setYesterdayView(newsView.getYesterdayView() + 1);
                }
                if(viewsRecord.getViewsType() == ViewsEnum.POLITICS){
                    politicsView.setYesterdayView(politicsView.getYesterdayView() + 1);
                }
                if(viewsRecord.getViewsType() == ViewsEnum.SOCIETY){
                    societyView.setYesterdayView(societyView.getYesterdayView() + 1);
                }
                if(viewsRecord.getViewsType() == ViewsEnum.TOPIC){
                    topicViews.setYesterdayView(topicViews.getYesterdayView() + 1);
                }
            }
        }

        viewReportResp.setTopicViews(topicViews);
        viewReportResp.setPoliticsView(politicsView);
        viewReportResp.setNewsView(newsView);
        viewReportResp.setSocietyView(societyView);
        viewReportResp.setSoutheastView(southeastView);

        southeastView.calcRates();
        newsView.calcRates();
        politicsView.calcRates();
        societyView.calcRates();
        topicViews.calcRates();

        return viewReportResp;
    }

}
