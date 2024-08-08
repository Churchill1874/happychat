package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.common.constant.enums.NewsCategoryEnum;
import com.ent.happychat.common.constant.enums.NewsStatusEnum;
import com.ent.happychat.entity.base.UpdateBaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("news")
@ApiModel("新闻")
public class News extends UpdateBaseInfo implements Serializable {
    private static final long serialVersionUID = 2788566232849282370L;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("过滤内容")
    private String filterContent;

    @ApiModelProperty("来源")
    private String source;

    @ApiModelProperty("类型")
    private NewsCategoryEnum category;

    @ApiModelProperty("三方图片路径")
    private String photoPath;

    @ApiModelProperty("提取内容的图片路径")
    private String contentImagePath;

    @ApiModelProperty("原新闻地址,跳转地址")
    private String url;

    @ApiModelProperty("浏览量")
    private Integer viewCount = 0;

    @ApiModelProperty("点赞数量")
    private Integer likesCount = 0;

    @ApiModelProperty("被评论数量")
    private Integer commentsCount = 0;

    @ApiModelProperty("新闻状态")
    private NewsStatusEnum newsStatus;

}
