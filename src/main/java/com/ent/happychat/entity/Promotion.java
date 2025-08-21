package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.common.constant.enums.PromotionTypeEnum;
import com.ent.happychat.entity.base.BaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("帮推广")
@TableName("promotion")
public class Promotion extends BaseInfo implements Serializable {
    private static final long serialVersionUID = 362828520285193131L;

    @ApiModelProperty("推广内容类型")
    private PromotionTypeEnum type;

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

    @ApiModelProperty("联系方式 最多两个")
    private String contact;

    @ApiModelProperty("区域")
    private String area;

    @ApiModelProperty("展示状态")
    private Boolean status;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("价格描述")
    private String price;

}
