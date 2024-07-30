package com.ent.happychat.pojo.resp.news;

import com.ent.happychat.entity.News;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class HomeNewsResp implements Serializable {
    private static final long serialVersionUID = -8655985577245382380L;

    @ApiModelProperty("滚动新闻")
    private List<News> newsList;

    @ApiModelProperty("置顶新闻")
    private News topNews;

    @ApiModelProperty("在线人数")
    private int onlinePlayerCount;

    @ApiModelProperty("轮播图列表")
    private List<String> bannerPathList;

}
