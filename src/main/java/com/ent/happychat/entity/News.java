package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.common.constant.enums.NewsCategoryEnum;
import com.ent.happychat.common.constant.enums.NewsStatusEnum;
import com.ent.happychat.entity.base.CreatorBaseInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("news")
@ApiModel("新闻")
public class News extends CreatorBaseInfo implements Serializable {
    private static final long serialVersionUID = 2788566232849282370L;

    @TableField("title")
    @ApiModelProperty("标题")
    private String title;

    @TableField("content")
    @ApiModelProperty("内容")
    private String content;

    @TableField("filter_content")
    @ApiModelProperty("过滤内容")
    private String filterContent;

    @TableField("source")
    @ApiModelProperty("来源")
    private String source;

    @TableField("category")
    @ApiModelProperty("类型")
    private NewsCategoryEnum category;

    @TableField("photo_path")
    @ApiModelProperty("三方图片路径")
    private String photoPath;

    @TableField("content_image_path")
    @ApiModelProperty("提取内容的图片路径")
    private String contentImagePath;

    @TableField("url")
    @ApiModelProperty("原新闻地址,跳转地址")
    private String url;

    @TableField("view_count")
    @ApiModelProperty("浏览量")
    private Integer viewCount = 0;

    @TableField("likes_count")
    @ApiModelProperty("点赞数量")
    private Integer likesCount = 0;

    @TableField("bad_count")
    @ApiModelProperty("点坏数量")
    private Integer badCount = 0;

    @TableField("comments_count")
    @ApiModelProperty("评论数量")
    private Integer commentsCount = 0;

    @TableField("news_status")
    @ApiModelProperty("新闻状态")
    private NewsStatusEnum newsStatus;

}
