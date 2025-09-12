package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.common.constant.enums.LevelEnum;
import com.ent.happychat.entity.base.BaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("tieba")
@ApiModel("贴吧")
public class Tieba extends BaseInfo implements Serializable {
    private static final long serialVersionUID = 100564171902550119L;
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("玩家id")
    private Long playerId;
    @ApiModelProperty("玩家账号")
    private String account;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("地址")
    private String address;
    @ApiModelProperty("等级")
    private LevelEnum level;
    @ApiModelProperty("是否置顶")
    private Boolean isTop;
    @ApiModelProperty("是否热门")
    private Boolean isHot;
    @ApiModelProperty("内容")
    private String content;
    @ApiModelProperty("图片1")
    private String image1;
    @ApiModelProperty("图片2")
    private String image2;
    @ApiModelProperty("图片3")
    private String image3;
    @ApiModelProperty("图片4")
    private String image4;
    @ApiModelProperty("图片5")
    private String image5;
    @ApiModelProperty("图片6")
    private String image6;
    @ApiModelProperty("评论数量")
    private Integer commentCount;
    @ApiModelProperty("展示数量")
    private Integer viewCount;
    @ApiModelProperty("点赞数量")
    private Integer likesCount;
    @ApiModelProperty("最后一次评论时间")
    private LocalDateTime lastCommentTime;


}
