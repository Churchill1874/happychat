package com.ent.happychat.pojo.req.news;

import com.ent.happychat.common.constant.enums.NewsCategoryEnum;
import com.ent.happychat.common.constant.enums.NewsStatusEnum;
import com.ent.happychat.pojo.req.PageBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class NewsPageReq extends PageBase implements Serializable {
    private static final long serialVersionUID = -3968897108907540868L;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("新闻类别")
    private NewsCategoryEnum categoryEnum;

    @ApiModelProperty("新闻状态")
    private NewsStatusEnum newsStatus;

    @ApiModelProperty("是否按照浏览量降序")
    private Boolean viewSort = false;

    @ApiModelProperty("是否按照被赞量降序")
    private Boolean likesSort = false;

    @ApiModelProperty("是否按照评论数量降序")
    private Boolean commentsSort = false;


}
