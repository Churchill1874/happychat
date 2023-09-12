package com.ent.happychat.pojo.req.footballmatch;

import com.ent.happychat.config.BigDecimalSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FootballMatchAddReq implements Serializable {

    private static final long serialVersionUID = -6743136321782140974L;

    @NotBlank(message = "赛事系列不能为空")
    @ApiModelProperty(value = "系列 如西甲,英超,德甲,法甲,中超,国际友谊赛,世界杯等", required = true)
    private String series;

    @NotBlank(message = "主队名称不能为空")
    @ApiModelProperty(value = "主队", required = true)
    private String homeTeam;

    @NotNull(message = "主队赔率不能为空")
    @ApiModelProperty(value = "主队胜赔率", required = true)
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal homeTeamOdds;

    @NotBlank(message = "客队名称不能为空")
    @ApiModelProperty(value = "客队", required = true)
    private String visitingTeam;

    @NotNull(message = "客队赔率不能为空")
    @ApiModelProperty(value = "客队胜赔率", required = true)
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal visitingTeamOdds;

    @NotNull(message = "平局赔率不能为空")
    @ApiModelProperty(value = "平局赔率", required = true)
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal drawOdds;

    @NotNull(message = "让球情况不能为空")
    @ApiModelProperty(value = "让球 例: -1 为主队让1秋,1 为客队让1球 ,0则无让球", required = true)
    private Integer adjustScore;

    @NotNull(message = "开始时间不能为空")
    @ApiModelProperty(value = "比赛开始时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

}
