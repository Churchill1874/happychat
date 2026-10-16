package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.entity.base.UpdateBaseInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("投票")
@TableName("vote")
public class Vote extends UpdateBaseInfo implements Serializable {
    private static final long serialVersionUID = 4127703385512870331L;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("投票事件简介")
    private String description;

    @ApiModelProperty("封面图")
    private String coverImagePath;

    @ApiModelProperty("是否多选")
    private Boolean multiSelect;

    @ApiModelProperty("统计截止时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;

    @ApiModelProperty("参与投票人数")
    private Integer voterCount;

    @ApiModelProperty("浏览数")
    private Integer viewCount;

    @ApiModelProperty("评论数")
    private Integer commentsCount;

    @ApiModelProperty("是否置顶")
    private Boolean isTop;

    @ApiModelProperty("展示状态")
    private Boolean status;

}
