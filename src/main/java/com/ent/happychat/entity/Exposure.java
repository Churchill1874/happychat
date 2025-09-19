package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.entity.base.BaseInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("exposure")
public class Exposure extends BaseInfo implements Serializable {
    private static final long serialVersionUID = 576789121577137022L;

    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("内容")
    private String content;
    @ApiModelProperty("是否置顶")
    private Boolean isTop;

    @ApiModelProperty("图片1")
    private String image1;
    @ApiModelProperty("声纹人员名称1")
    private String username1;
    @ApiModelProperty("声音1")
    private String sound1;

    @ApiModelProperty("图片2")
    private String image2;
    @ApiModelProperty("声纹人员名称2")
    private String username2;
    @ApiModelProperty("声音2")
    private String sound2;

    @ApiModelProperty("图片3")
    private String image3;
    @ApiModelProperty("声纹人员名称3")
    private String username3;
    @ApiModelProperty("声音3")
    private String sound3;

    @ApiModelProperty("图片4")
    private String image4;
    @ApiModelProperty("声纹人员名称4")
    private String username4;
    @ApiModelProperty("声音4")
    private String sound4;

    @ApiModelProperty("图片5")
    private String image5;
    @ApiModelProperty("声纹人员名称5")
    private String username5;
    @ApiModelProperty("声音5")
    private String sound5;

    @ApiModelProperty("图片6")
    private String image6;
    @ApiModelProperty("声纹人员名称6")
    private String username6;
    @ApiModelProperty("声音6")
    private String sound6;

    @ApiModelProperty("浏览次数")
    private Integer viewsCount;
    @ApiModelProperty("等级")
    private Integer level;
    @ApiModelProperty("事件地点")
    private String address;



}
