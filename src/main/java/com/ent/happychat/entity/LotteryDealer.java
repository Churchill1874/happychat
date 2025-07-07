package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.common.constant.enums.LotteryTypeEnum;
import com.ent.happychat.entity.base.BaseInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("lottery_dealer")
public class LotteryDealer extends BaseInfo implements Serializable {
    private static final long serialVersionUID = 7667141120745212230L;

    @ApiModelProperty("投注数量")
    private Integer betCount;
    @ApiModelProperty("彩种类型")
    private LotteryTypeEnum type;
    @ApiModelProperty("彩种信息关联外键")
    private Long lotteryId;
    @ApiModelProperty("庄家用户id")
    private Long playerId;
    @ApiModelProperty("奖池")
    private BigDecimal prizePool;
    @ApiModelProperty("剩余奖池")
    private BigDecimal remainingPrizePool;
    @ApiModelProperty("选择1赔率")
    private BigDecimal odds1;
    @ApiModelProperty("选择2赔率")
    private BigDecimal odds2;
    @ApiModelProperty("选择3赔率")
    private BigDecimal odds3;

    @ApiModelProperty("下注支持率1")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal rate1;
    @ApiModelProperty("下注支持率2")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal rate2;
    @ApiModelProperty("下注支持率3")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal rate3;

}
