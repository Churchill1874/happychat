package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.common.constant.enums.LotteryStatusEnum;
import com.ent.happychat.entity.base.BaseInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("politics_lottery")
public class PoliticsLottery extends BaseInfo implements Serializable {
    private static final long serialVersionUID = -6758977697836845724L;

    @ApiModelProperty("选择1")
    private String choose1;
    @ApiModelProperty("描述1")
    private String describe1;
    @ApiModelProperty("图标1")
    private String icon1;

    @ApiModelProperty("选择2")
    private String choose2;
    @ApiModelProperty("描述2")
    private String describe2;
    @ApiModelProperty("图标2")
    private String icon2;

    @ApiModelProperty("选择3")
    private String choose3;
    @ApiModelProperty("描述3")
    private String describe3;
    @ApiModelProperty("图标3")
    private String icon3;

    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("规则简介")
    private String rule;
    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    @ApiModelProperty("是否开售")
    private Boolean isOnSale;
    @ApiModelProperty("开奖结果")
    private Integer result;
    @ApiModelProperty("彩票状态")
    private LotteryStatusEnum status;


}
