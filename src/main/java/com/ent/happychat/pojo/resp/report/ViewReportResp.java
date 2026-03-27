package com.ent.happychat.pojo.resp.report;

import lombok.Data;
/**新闻浏览统计**/
@Data
public class ViewReportResp {

    private ViewInfoResp southeastView;

    private ViewInfoResp newsView;

    private ViewInfoResp politicsView;

    private ViewInfoResp societyView;

    private ViewInfoResp topicViews;


}
