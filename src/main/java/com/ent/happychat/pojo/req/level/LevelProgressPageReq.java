package com.ent.happychat.pojo.req.level;

import com.ent.happychat.pojo.req.PageBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class LevelProgressPageReq extends PageBase implements Serializable {
    private static final long serialVersionUID = 7922572006833522672L;

    @ApiModelProperty("玩家id")
    private Long playerId;

}
