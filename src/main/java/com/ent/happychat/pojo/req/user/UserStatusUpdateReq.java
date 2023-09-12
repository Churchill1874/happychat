package com.ent.happychat.pojo.req.user;

import com.ent.happychat.pojo.Id;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class UserStatusUpdateReq extends Id implements Serializable {

    private static final long serialVersionUID = 6461153382446177771L;

    @NotNull(message = "状态不能为空")
    @ApiModelProperty("1正常 0禁用")
    private Integer status;

}
