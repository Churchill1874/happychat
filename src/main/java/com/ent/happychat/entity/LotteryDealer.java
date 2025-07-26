package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.common.constant.enums.LotteryTypeEnum;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.entity.base.BaseInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@TableName("lottery_dealer")
public class LotteryDealer extends BaseInfo implements Serializable {
    private static final long serialVersionUID = 7667141120745212230L;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    @ApiModelProperty("选项1投注总额")
    private BigDecimal bet1Amount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    @ApiModelProperty("选项2投注总额")
    private BigDecimal bet2Amount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    @ApiModelProperty("选项3投注总额")
    private BigDecimal bet3Amount;
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
    @ApiModelProperty("下注总数量")
    private Integer betCount;

    public BigDecimal findOdds(Integer chooseNumber) {
        if (chooseNumber == 1) {
            return this.odds1;
        }
        if (chooseNumber == 2) {
            return this.odds2;
        }
        if (chooseNumber == 3) {
            return this.odds3;
        }
        throw new DataException("下注号码异常");
    }

    public void countBetAmount(Integer chooseNumber, BigDecimal betAmount){
        if (chooseNumber == 1){
            this.bet1Amount = this.bet1Amount.add(betAmount);
        }
        if (chooseNumber == 2){
            this.bet2Amount = this.bet2Amount.add(betAmount);
        }
        if (chooseNumber == 3){
            this.bet3Amount = this.bet3Amount.add(betAmount);
        }
    }

    public BigDecimal getWinnerAmount(Integer chooseNumber, BigDecimal betAmount) {
        BigDecimal winnerAmount = findOdds(chooseNumber).multiply(betAmount);
        if (winnerAmount.compareTo(this.remainingPrizePool) > 0) {
            throw new DataException("剩余奖池不足,请降低投注金额");
        }
        return winnerAmount;
    }

    //计算比例
    public void calculateProportion() {
        this.betCount = this.betCount + 1;

        BigDecimal betTotalAmount = this.bet1Amount.add(this.bet2Amount).add(this.bet3Amount);

        if (this.bet1Amount.compareTo(BigDecimal.ZERO) > 0) {
            this.rate1 = this.bet1Amount.divide(betTotalAmount, 2, RoundingMode.DOWN).multiply(new BigDecimal("100"));
        } else {
            this.rate1 = new BigDecimal(0);
        }

        if (this.bet2Amount.compareTo(BigDecimal.ZERO) > 0) {
            this.rate2 = this.bet2Amount.divide(betTotalAmount, 2, RoundingMode.DOWN).multiply(new BigDecimal("100"));
        } else {
            this.rate2 = new BigDecimal(0);
        }

        if (this.bet3Amount.compareTo(BigDecimal.ZERO) > 0) {
            this.rate3 = bet3Amount.divide(betTotalAmount, 2, RoundingMode.DOWN).multiply(new BigDecimal("100"));
        } else {
            this.rate3 = new BigDecimal(0);
        }
    }

}
