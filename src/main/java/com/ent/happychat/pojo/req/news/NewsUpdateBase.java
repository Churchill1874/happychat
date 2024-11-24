package com.ent.happychat.pojo.req.news;

import com.ent.happychat.common.constant.enums.NewsStatusEnum;
import com.ent.happychat.pojo.req.IdBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class NewsUpdateBase extends IdBase implements Serializable {
    private static final long serialVersionUID = 3789407400163748880L;

    @ApiModelProperty("新闻状态")
    private NewsStatusEnum newsStatus;

    @ApiModelProperty("点赞数量")
    private Integer likesCount = 0;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("来源")
    private String source;

    @ApiModelProperty("提取内容的图片路径")
    private String contentImagePath;

    @ApiModelProperty("浏览量")
    private Integer viewCount = 0;



}
