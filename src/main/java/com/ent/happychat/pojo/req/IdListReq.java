package com.ent.happychat.pojo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Data
public class IdListReq implements Serializable {

    private static final long serialVersionUID = -8260779700667025387L;

    @NotEmpty(message = "id集合不能为空")
    @ApiModelProperty("id集合")
    private List<Long> idList;

}
