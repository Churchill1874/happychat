package com.ent.happychat.pojo.req.betorder;

import com.ent.happychat.common.constant.enums.LotteryTypeEnum;
import com.ent.happychat.pojo.req.PageBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class BetOrderPageReq extends PageBase implements Serializable {
    private static final long serialVersionUID = 2512039061419601356L;

    @ApiModelProperty("盘口id")
    private Long dealerId;

    @NotNull(message = "类型不能为空")
    @ApiModelProperty("彩种类型")
    private LotteryTypeEnum type;

    private Long playerId;

}
