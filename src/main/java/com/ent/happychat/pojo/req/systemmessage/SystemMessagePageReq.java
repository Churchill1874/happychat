package com.ent.happychat.pojo.req.systemmessage;

import com.ent.happychat.common.constant.enums.InfoEnum;
import com.ent.happychat.common.constant.enums.MessageTypeEnum;
import com.ent.happychat.pojo.req.PageBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SystemMessagePageReq extends PageBase implements Serializable {
    private static final long serialVersionUID = -1169138408748818127L;

    @ApiModelProperty("消息类型")
    private MessageTypeEnum messageType;

    @ApiModelProperty("收取人账号")
    private String recipientAccount;

    @ApiModelProperty("发送人账号")
    private String senderAccount;

    @ApiModelProperty("新闻id")
    private Long newsId;

    @ApiModelProperty("消息来源")
    private InfoEnum sourceType;

}
