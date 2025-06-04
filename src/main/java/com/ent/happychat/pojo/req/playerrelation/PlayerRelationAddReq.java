package com.ent.happychat.pojo.req.playerrelation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class PlayerRelationAddReq implements Serializable {
    private static final long serialVersionUID = 2455263526255362485L;

    @NotNull(message = "玩家不能为空")
    @ApiModelProperty("玩家id")
    private Long playerId;

    @NotNull(message = "目标玩家不能为空")
    @ApiModelProperty("目标玩家id")
    private Long targetPlayerId;

}
