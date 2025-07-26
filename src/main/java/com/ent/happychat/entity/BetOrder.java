package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.common.constant.enums.DrawStatusEnum;
import com.ent.happychat.common.constant.enums.LevelEnum;
import com.ent.happychat.common.constant.enums.LotteryTypeEnum;
import com.ent.happychat.entity.base.BaseInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("bet_order")
public class BetOrder extends BaseInfo implements Serializable {
    private static final long serialVersionUID = -3840221494677807249L;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("彩种类型")
    private LotteryTypeEnum type;

    @ApiModelProperty("盘口id")
    private Long dealerId;

    @ApiModelProperty("下注玩家id")
    private Long playerId;

    @ApiModelProperty("玩家名称")
    private String playerName;

    @ApiModelProperty("玩家等级")
    private LevelEnum playerLevel;

    @ApiModelProperty("玩家头像")
    private String playerAvatar;

    @ApiModelProperty("下注号码")
    private Integer chooseNumber;

    @ApiModelProperty("下注内容")
    private String choose;

    @ApiModelProperty("赔率")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal odds;

    @ApiModelProperty("庄家用户id")
    private Long dealerUserId;

    @ApiModelProperty("庄家昵称")
    private String dealerUsername;

    @ApiModelProperty("等级")
    private LevelEnum dealerUserLevel;

    @ApiModelProperty("庄家头像")
    private String dealerAvatar;

    @ApiModelProperty("投注金额")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal betAmount;

    @ApiModelProperty("奖金")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal amount;

    @TableField("draw_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime drawTime;

    @ApiModelProperty("开奖状态")
    private DrawStatusEnum status;

}
