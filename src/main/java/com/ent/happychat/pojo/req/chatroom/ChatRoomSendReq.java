package com.ent.happychat.pojo.req.chatroom;

import com.ent.happychat.common.constant.enums.ChatContentEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ChatRoomSendReq implements Serializable {
    private static final long serialVersionUID = -7647634403842444428L;

    @ApiModelProperty("房间号")
    private Integer roomNumber;

    @ApiModelProperty("发送人")
    private Long playerId;

    @ApiModelProperty("消息接收人")
    private Long targetPlayerId;

    @ApiModelProperty("发送内容")
    private String content;

    @ApiModelProperty("回复id")
    private Long replyId;

    @ApiModelProperty("回复内容")
    private String replyContent;

    @ApiModelProperty("内容类型")
    private ChatContentEnum type;

}
