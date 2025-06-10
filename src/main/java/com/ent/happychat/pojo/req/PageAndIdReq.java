package com.ent.happychat.pojo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class PageAndIdReq extends PageBase implements Serializable {
    private static final long serialVersionUID = 4585447782185120205L;
    @NotNull(message = "分页条件id不能为空")
    @ApiModelProperty(value = "id", required = true)
    private Long id;
}
