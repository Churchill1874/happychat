package com.ent.happychat.pojo.req.systemmessage;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SystemMessageDelete {

    @ApiModelProperty("标题")
    private String title;

}
