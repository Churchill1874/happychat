package com.ent.happychat.pojo.resp.news;

import com.ent.happychat.entity.News;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

@Data
public class HomeNews implements Serializable {
    private static final long serialVersionUID = -8655985577245382380L;

    @ApiModelProperty("滚动新闻")
    private List<News> newsList;

    @ApiModelProperty("置顶新闻")
    private News topNews;

    @ApiModelProperty("热门头条")
    private News hotNews;

}
