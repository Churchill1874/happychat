package com.ent.happychat.pojo.resp.views;

import com.ent.happychat.common.constant.enums.LevelEnum;
import com.ent.happychat.entity.ViewsRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ViewsRecordPageResp extends ViewsRecord implements Serializable {
    private static final long serialVersionUID = -8488149247948198080L;

    @ApiModelProperty("玩家名称")
    private String playerName;
    @ApiModelProperty("玩家账号")
    private String account;
    @ApiModelProperty("等级")
    private LevelEnum level;

}
