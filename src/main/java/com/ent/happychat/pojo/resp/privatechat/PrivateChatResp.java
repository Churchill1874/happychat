package com.ent.happychat.pojo.resp.privatechat;

import com.ent.happychat.common.constant.enums.LevelEnum;
import com.ent.happychat.entity.PrivateChat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PrivateChatResp extends PrivateChat implements Serializable {
    private static final long serialVersionUID = -6955480379052382062L;

    @ApiModelProperty("发送人名字")
    private String sendName;

    @ApiModelProperty("接收人名字")
    private String receiveName;

    @ApiModelProperty("发送人头像路径")
    private String sendAvatarPath;

    @ApiModelProperty("接收人头像路径")
    private String receiveAvatarPath;

    @ApiModelProperty("发送人等级")
    private LevelEnum sendLevel;

    @ApiModelProperty("接收人等级")
    private LevelEnum receiveLevel;

    @ApiModelProperty("未读取")
    private Boolean notRead;

}
