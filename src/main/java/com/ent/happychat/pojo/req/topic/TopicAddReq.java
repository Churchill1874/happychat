package com.ent.happychat.pojo.req.topic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class TopicAddReq implements Serializable {
    private static final long serialVersionUID = 895133427848543841L;

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

}
