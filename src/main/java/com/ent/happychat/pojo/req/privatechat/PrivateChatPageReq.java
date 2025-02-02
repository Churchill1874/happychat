package com.ent.happychat.pojo.req.privatechat;

import com.ent.happychat.pojo.req.PageBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PrivateChatPageReq extends PageBase implements Serializable {
    private static final long serialVersionUID = 7010737825177408368L;

    @ApiModelProperty("发送人账号")
    private String sendAccount;

    @ApiModelProperty("接收人账号")
    private String receiveAccount;

}
