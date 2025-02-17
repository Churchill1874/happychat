package com.ent.happychat.pojo.resp.privatechat;

import com.ent.happychat.entity.PrivateChat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PrivateChatResp extends PrivateChat implements Serializable {
    private static final long serialVersionUID = 4236054245828742810L;

    @ApiModelProperty("该聊天记录 是否是当前登录用户发送")
    private Boolean isSender;

}
