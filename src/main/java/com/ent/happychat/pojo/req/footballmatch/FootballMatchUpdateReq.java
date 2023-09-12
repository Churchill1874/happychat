package com.ent.happychat.pojo.req.footballmatch;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class FootballMatchUpdateReq extends FootballMatchAddReq implements Serializable {

    private static final long serialVersionUID = -2583390085497845819L;

    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id", required = true)
    private Long id;

}
