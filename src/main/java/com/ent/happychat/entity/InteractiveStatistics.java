package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("interactive_statistics")
public class InteractiveStatistics implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty("玩家id")
    private Long playerId;

    @ApiModelProperty("粉丝数量")
    private Long followersCount;

    @ApiModelProperty("关注数量")
    private Long collectCount;

    @ApiModelProperty("被点赞数量")
    private Long likesReceivedCount;


}
