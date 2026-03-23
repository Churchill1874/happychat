package com.ent.happychat.pojo.req.society;

import com.ent.happychat.pojo.req.IdBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SocietyUpdateReq extends IdBase implements Serializable {
    private static final long serialVersionUID = 895133427848543841L;

    @ApiModelProperty(value = "新闻来源",required = true)
    private String source;

    @ApiModelProperty(value = "内容",required = true)
    private String content;

    @ApiModelProperty("图片路径")
    private String imagePath;

    @ApiModelProperty("阅读数量")
    private Integer viewCount;

    @ApiModelProperty("评论数量")
    private Integer commentsCount;

    @ApiModelProperty("是否置顶")
    private Boolean isTop;

    @ApiModelProperty("是否热门")
    private Boolean isHot;

    @ApiModelProperty(value = "区域",required = true)
    private String area;

    @ApiModelProperty("展示状态")
    private Boolean status;

    @ApiModelProperty("视频路径")
    private String videoPath;

    @ApiModelProperty("视频封面")
    private String videoCover;

    @ApiModelProperty("标题")
    private String title;

}
