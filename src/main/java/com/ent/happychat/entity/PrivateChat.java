package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.entity.base.BaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
@TableName("private_chat")
@ApiModel("私信聊天")
public class PrivateChat extends BaseInfo implements Serializable {
    private static final long serialVersionUID = 3928476344494927484L;

    @ApiModelProperty("发送人账号")
    private String sendAccount;

    @ApiModelProperty("接收人账号")
    private String receiveAccount;

    @ApiModelProperty("私信内容")
    private String content;

    @ApiModelProperty("是否已经读取")
    private Boolean status;

}
