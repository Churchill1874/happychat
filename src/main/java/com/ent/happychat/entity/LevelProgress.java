package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.common.constant.enums.LevelEnum;
import com.ent.happychat.entity.base.BaseInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
@TableName("level_progress")
public class LevelProgress extends BaseInfo implements Serializable {
    private static final long serialVersionUID = -6641322456222960618L;

    @ApiModelProperty("玩家id")
    private Long playerId;

    @ApiModelProperty("当前等级")
    private LevelEnum level;

    @ApiModelProperty("被点赞数量")
    private Integer likesReceivedCount;

    @ApiModelProperty("下注正确数量")
    private Integer correctCount;

}
