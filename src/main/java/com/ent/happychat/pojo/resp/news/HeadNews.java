package com.ent.happychat.pojo.resp.news;

import com.ent.happychat.entity.News;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class HeadNews implements Serializable {
    private static final long serialVersionUID = -8655985577245382380L;

    @ApiModelProperty("热门新闻")
    private List<News> hotNews;

    @ApiModelProperty("置顶新闻")
    private List<News> topNews;

}
