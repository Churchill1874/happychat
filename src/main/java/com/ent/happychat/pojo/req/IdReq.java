package com.ent.happychat.pojo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@Data
public class IdReq implements Serializable {

    private static final long serialVersionUID = -9147895635279259749L;

    @Positive(message = "id数值错误")
    @NotNull(message = "id不能为空")
    @ApiModelProperty("id")
    private Long id;

}
