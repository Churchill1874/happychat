package com.ent.happychat.pojo.resp.privatechat;

import com.ent.happychat.common.constant.enums.LevelEnum;
import com.ent.happychat.entity.PrivateChat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PrivateChatResp extends PrivateChat implements Serializable {
    private static final long serialVersionUID = 4236054245828742810L;

    @ApiModelProperty("该聊天记录 是否是当前登录用户发送")
    private Boolean isSender;

    @ApiModelProperty("发消息人的头像")
    private String sendAvatarPath;
    @ApiModelProperty("账号")
    private String sendAccount;
    @ApiModelProperty("发送人昵称")
    private String sendName;
    @ApiModelProperty("收件人账号")
    private String receiverAccount;
    @ApiModelProperty("接收人昵称")
    private String receiveName;
    @ApiModelProperty("接收人的头像")
    private String receiveAvatarPath;

    @ApiModelProperty("发送人等级")
    private LevelEnum sendLevel;   // 新增

    @ApiModelProperty("接收人等级")
    private LevelEnum receiveLevel; // 新增
}
