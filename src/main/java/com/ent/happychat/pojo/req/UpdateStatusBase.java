package com.ent.happychat.pojo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class UpdateStatusBase extends Id implements Serializable {
    private static final long serialVersionUID = -2546611375841972726L;

    @NotNull(message = "状态不能为空")
    @ApiModelProperty("状态是否为禁用 true为正常 false禁用")
    private Boolean status;

}
