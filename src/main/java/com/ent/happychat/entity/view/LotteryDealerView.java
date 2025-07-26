package com.ent.happychat.entity.view;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.common.constant.enums.LevelEnum;
import com.ent.happychat.common.constant.enums.LotteryStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 视图表
 */
@Data
@TableName("lottery_dealer_view")
public class LotteryDealerView implements Serializable {
    private static final long serialVersionUID = 1413020065538379210L;

    @ApiModelProperty("彩票事件id")
    private Long id;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("选项1投注总额")
    private BigDecimal bet1Amount;
    @ApiModelProperty("选项2投注总额")
    private BigDecimal bet2Amount;
    @ApiModelProperty("选项3投注总额")
    private BigDecimal bet3Amount;
    @ApiModelProperty("投注总数")
    private Integer betCount;

    @ApiModelProperty("选择1")
    private String choose1;
    @ApiModelProperty("描述1")
    private String describe1;
    @ApiModelProperty("选择1赔率")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal odds1;
    @ApiModelProperty("下注支持率1")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal rate1;
    @ApiModelProperty("图标1")
    private String icon1;


    @ApiModelProperty("选择2")
    private String choose2;
    @ApiModelProperty("描述2")
    private String describe2;
    @ApiModelProperty("选择2赔率")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal odds2;
    @ApiModelProperty("下注支持率2")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal rate2;
    @ApiModelProperty("图标2")
    private String icon2;

    @ApiModelProperty("选择3")
    private String choose3;
    @ApiModelProperty("描述3")
    private String describe3;
    @ApiModelProperty("选择3赔率")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal odds3;
    @ApiModelProperty("下注支持率3")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal rate3;
    @ApiModelProperty("图标3")
    private String icon3;

    @ApiModelProperty("彩票状态")
    private LotteryStatusEnum status;
    @ApiModelProperty("开奖结果")
    private Integer result;
    @ApiModelProperty("可下注结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    @ApiModelProperty("规则简介")
    private String rule;
    @ApiModelProperty("开盘id")
    private Long dealerId;
    @ApiModelProperty("庄家用户id")
    private Long playerId;
    @ApiModelProperty("昵称")
    private String playerName;
    @ApiModelProperty("等级")
    private LevelEnum playerLevel;
    @ApiModelProperty("头像路径")
    private String playerAvatar;
    @ApiModelProperty("账号")
    private String playerAccount;


    @ApiModelProperty("奖池")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal prizePool;
    @ApiModelProperty("剩余奖池")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal remainingPrizePool;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @ApiModelProperty("开奖时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime drawTime;

}
