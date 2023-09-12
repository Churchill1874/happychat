package com.ent.happychat.pojo.req.footballmatch;

import com.ent.happychat.pojo.Id;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class FootballMatchScoreUpdateReq extends Id implements Serializable {

    private static final long serialVersionUID = -7917646859123730709L;

    @NotNull(message = "主队分数不能为空")
    @ApiModelProperty(value = "主队分数")
    private Integer homeTeamScore;

    @NotNull(message = "客队分数不能为空")
    @ApiModelProperty("客队分数")
    private Integer visitingTeamScore;

}
