package com.ent.happychat.pojo.req.playerrelation;

import com.ent.happychat.pojo.req.PageBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PlayerRelationPageReq extends PageBase implements Serializable {
    private static final long serialVersionUID = 6277925927155164968L;

    @ApiModelProperty("玩家id")
    private Long playerId;

    @ApiModelProperty("目标玩家id")
    private Long targetPlayerId;

}
