package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.config.BigDecimalSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("赛事")
@TableName("football_match")
public class FootballMatch extends BaseInfo implements Serializable {

    private static final long serialVersionUID = 8307171112614000772L;

    @TableField("series")
    @ApiModelProperty("系列 如西甲,英超,德甲,法甲,中超,国际友谊赛,世界杯等")
    private String series;

    @TableField("home_team")
    @ApiModelProperty("主队")
    private String homeTeam;

    @TableField("visiting_team")
    @ApiModelProperty("客队")
    private String visitingTeam;

    @TableField("home_team_odds")
    @ApiModelProperty("主队胜赔率")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal homeTeamOdds;

    @TableField("visiting_team_odds")
    @ApiModelProperty("客队胜赔率")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal visitingTeamOdds;

    @TableField("draw_odds")
    @ApiModelProperty("平局赔率")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal drawOdds;

    @TableField("score")
    @ApiModelProperty("比赛分数")
    private String score;

    @TableField("result")
    @ApiModelProperty("结果 0平局 1主队胜 2客队胜")
    private Integer result;

    @TableField("adjust_score")
    @ApiModelProperty("让球 例: -1 为主队让1秋,1 为客队让1球 ,0则无让球")
    private Integer adjustScore;

    @TableField("start_time")
    @ApiModelProperty("比赛开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;

    @TableField("end_time")
    @ApiModelProperty("比赛结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    @TableField("status")
    @ApiModelProperty("状态 0未开始 1比赛中 2比赛结束")
    private Integer status;

    @TableField("create_name")
    @ApiModelProperty("创建人")
    private String createName;

}
