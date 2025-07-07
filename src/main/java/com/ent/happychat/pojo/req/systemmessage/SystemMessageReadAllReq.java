package com.ent.happychat.pojo.req.systemmessage;

import com.ent.happychat.common.constant.enums.MessageTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class SystemMessageReadAllReq implements Serializable {

    private static final long serialVersionUID = -1517870648275817156L;

    @NotNull(message = "类型不能为空")
    @ApiModelProperty("信息类型")
    private MessageTypeEnum type;
}
