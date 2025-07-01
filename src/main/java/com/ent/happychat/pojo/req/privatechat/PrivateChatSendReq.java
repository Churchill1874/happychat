package com.ent.happychat.pojo.req.privatechat;

import com.ent.happychat.common.constant.enums.ChatContentEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PrivateChatSendReq implements Serializable {
    private static final long serialVersionUID = -1906829437409984311L;

    @ApiModelProperty("发送人账号")
    private Long sendId;

    @ApiModelProperty("消息接收人账号")
    private Long receiveId;

    @ApiModelProperty("发送内容")
    private String content;

    @ApiModelProperty("内容类型")
    private ChatContentEnum type;


}
