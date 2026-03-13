package com.ent.happychat.pojo.req.topic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class TopicUpdateReq implements Serializable {
    private static final long serialVersionUID = -7130770164692503446L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty(value = "话题类型描述",required = true)
    private String type;

    @ApiModelProperty(value = "内容",required = true)
    private String content;

    @ApiModelProperty("图片路径")
    private String imagePath;

    @ApiModelProperty("阅读数量")
    private Integer viewCount;

    @ApiModelProperty("评论数量")
    private Integer commentCount;

    @ApiModelProperty("是否置顶")
    private Boolean isTop;

    @ApiModelProperty("是否热门")
    private Boolean isHot;

    @ApiModelProperty("展示状态")
    private Boolean status;

    @ApiModelProperty("视频路径")
    private String videoPath;

    @ApiModelProperty("视频封面")
    private String videoCover;

}
