package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.entity.base.BaseInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("投票选项")
@TableName("vote_option")
public class VoteOption extends BaseInfo implements Serializable {
    private static final long serialVersionUID = -2361850993164032713L;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("投票id")
    private Long voteId;

    @ApiModelProperty("选项内容")
    private String content;

    @ApiModelProperty("选项图片")
    private String imagePath;

    @ApiModelProperty("排序 越小越靠前")
    private Integer sort;

    @ApiModelProperty("得票数")
    private Integer voteCount;

}
