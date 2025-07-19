package com.ent.happychat.pojo.req.lottery.dealer;

import com.ent.happychat.pojo.req.IdBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class LotteryDealerAdd extends IdBase implements Serializable {
    private static final long serialVersionUID = -1395906472974315097L;

    @NotNull(message = "奖池不能为空")
    @ApiModelProperty("奖池")
    private BigDecimal prizePool;

    @NotNull(message = "赔率1不能为空")
    @ApiModelProperty("选择1赔率")
    private BigDecimal odds1;

    @NotNull(message = "赔率2不能为空")
    @ApiModelProperty("选择2赔率")
    private BigDecimal odds2;

    @ApiModelProperty("选择3赔率")
    private BigDecimal odds3;

}
