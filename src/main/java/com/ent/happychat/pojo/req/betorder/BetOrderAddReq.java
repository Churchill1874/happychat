package com.ent.happychat.pojo.req.betorder;

import com.ent.happychat.common.constant.enums.LevelEnum;
import com.ent.happychat.common.constant.enums.LotteryTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BetOrderAddReq implements Serializable {
    private static final long serialVersionUID = -354240410779904342L;

    @NotNull(message = "盘口id不能为空")
    @ApiModelProperty("盘口id")
    private Long dealerId;

    @NotNull(message = "下注号码不能为空")
    @ApiModelProperty("下注号码")
    private Integer chooseNumber;

    @NotNull(message = "投注金额不能为空")
    @ApiModelProperty("投注金额")
    private BigDecimal betAmount;

    private LevelEnum dealerUserLevel;

    private LotteryTypeEnum type;

    private Long playerId;

    private String playerName;

    private String playerAvatar;

    private LevelEnum playerLevel;
}
