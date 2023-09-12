package com.ent.happychat.pojo.req.footballmatch;

import com.ent.happychat.pojo.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class FootballMatchPageReq extends Page implements Serializable {

    private static final long serialVersionUID = -3350802543597078035L;

    @ApiModelProperty("系列 如西甲,英超,德甲,法甲,中超,国际友谊赛,世界杯等")
    private String series;

    @ApiModelProperty("搜索范围起始时间")
    private LocalDate startDate;

    @ApiModelProperty("搜索范围结束时间")
    private LocalDate endDate;

    @ApiModelProperty("状态 0未开始 1比赛中 2比赛结束")
    private Integer status;

    @ApiModelProperty("指定日期。注:当查指定日期的数据时,startDate和endDate应该为空")
    private LocalDate targetDate;

}
