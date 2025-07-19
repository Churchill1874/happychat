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

    @ApiModelProperty("选项1投注数量")
    private Integer count1;
    @ApiModelProperty("选项2投注数量")
    private Integer count2;
    @ApiModelProperty("选项3投注数量")
    private Integer count3;
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

    public BigDecimal getWinnerAmount(Integer chooseNumber, BigDecimal betAmount) {
        BigDecimal winnerAmount = findOdds(chooseNumber).multiply(betAmount);
        if (winnerAmount.compareTo(this.remainingPrizePool) > 0) {
            throw new DataException("剩余奖池不足以担保您的中奖金额");
        }
        return winnerAmount;
    }

    public void addCount(Integer chooseNumber) {
        if (chooseNumber == 1) {
            this.count1 = this.count1 + 1;
        }
        if (chooseNumber == 2) {
            this.count2 = this.count2 + 1;
        }
        if (chooseNumber == 3) {
            this.count3 = this.count3 + 1;
        }

        BigDecimal totalCount = new BigDecimal(this.count1 + this.count2 + this.count3);
        this.rate1 = totalCount.divide(new BigDecimal(this.count1), 2, RoundingMode.DOWN).multiply(new BigDecimal("100"));
        this.rate2 = totalCount.divide(new BigDecimal(this.count2), 2, RoundingMode.DOWN).multiply(new BigDecimal("100"));
        this.rate3 = totalCount.divide(new BigDecimal(this.count3), 2, RoundingMode.DOWN).multiply(new BigDecimal("100"));
    }

}
