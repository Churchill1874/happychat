package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.entity.base.BaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("话题")
@TableName("topic")
public class Topic extends BaseInfo implements Serializable {
    private static final long serialVersionUID = 362828520285193131L;

    @ApiModelProperty("类型描述")
    private String type;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("图片路径")
    private String imagePath;

    @ApiModelProperty("视频链接")
    private String videoPath;

    @ApiModelProperty("视频封面")
    private String videoCover;

    @ApiModelProperty("阅读数量")
    private Integer viewCount;

    @ApiModelProperty("评论数量")
    private Integer commentsCount;

    @ApiModelProperty("是否置顶")
    private Boolean isTop;

    @ApiModelProperty("是否热门")
    private Boolean isHot;

    @ApiModelProperty("展示状态")
    private Boolean status;

    @ApiModelProperty("标题")
    private String title;

}
