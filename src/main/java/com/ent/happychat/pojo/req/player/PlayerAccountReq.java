package com.ent.happychat.pojo.req.player;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class PlayerAccountReq implements Serializable {
    private static final long serialVersionUID = -8651580115398462114L;

    @NotBlank(message = "账号不能为空")
    @ApiModelProperty("账号")
    private String account;

}
