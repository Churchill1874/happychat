package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.common.constant.enums.NewsCategoryEnum;
import com.ent.happychat.common.constant.enums.NewsStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("news")
@ApiModel("新闻")
public class News extends BaseInfo implements Serializable {
    private static final long serialVersionUID = 2788566232849282370L;

    @TableField("title")
    @ApiModelProperty("标题")
    private String title;

    @TableField("content")
    @ApiModelProperty("内容")
    private String content;

    @TableField("source")
    @ApiModelProperty("来源")
    private String source;

    @TableField("category")
    @ApiModelProperty("类型")
    private NewsCategoryEnum category;

    @TableField("photo_path")
    @ApiModelProperty("图片路径")
    private String photoPath;

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


    @TableField("news_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime newsTime;

}
