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
@ApiModel("投票记录")
@TableName("vote_record")
public class VoteRecord extends BaseInfo implements Serializable {
    private static final long serialVersionUID = 6627165301839716585L;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("投票id")
    private Long voteId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("选项id")
    private Long optionId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("投票用户id")
    private Long playerId;

}
