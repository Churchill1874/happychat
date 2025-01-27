package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.entity.base.BaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("东南亚新闻")
@TableName("southeast_asia")
public class SoutheastAsia extends BaseInfo implements Serializable {
    private static final long serialVersionUID = 362828520285193131L;

    @ApiModelProperty("新闻来源")
    private String source;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("图片路径")
    private String imagePath;

    @ApiModelProperty("阅读数量")
    private Integer readCount;

    @ApiModelProperty("评论数量")
    private Integer commentCount;

    @ApiModelProperty("是否置顶")
    private Boolean isTop;

    @ApiModelProperty("是否热门")
    private Boolean isHot;

    @ApiModelProperty("区域")
    private String area;

    @ApiModelProperty("展示状态")
    private Boolean status;

    @ApiModelProperty("标题")
    private String title;


}
