package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.common.constant.enums.InfoEnum;
import com.ent.happychat.common.constant.enums.MessageTypeEnum;
import com.ent.happychat.entity.base.BaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("message")
@ApiModel("消息")
public class SystemMessage extends BaseInfo implements Serializable {
    private static final long serialVersionUID = 5655088707782304840L;

    @ApiModelProperty("信息类型messageType是评论类型的时候,title是自己发表对新闻的评论信息,现在有人对此评论发表评论." +
        "信息类型messageType为系统类型的时候,title是系统消息标题")
    private String title;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("是否已经读取")
    private Boolean status;

    @ApiModelProperty("消息类型")
    private MessageTypeEnum messageType;

    @ApiModelProperty("收取人账号")
    private Long recipientAccount;

    @ApiModelProperty("发送人id")
    private Long senderAccount;

    @ApiModelProperty("新闻id")
    private Long newsId;

    @ApiModelProperty("评论来源")
    private InfoEnum sourceType;

    @ApiModelProperty("图片路径")
    private String imagePath;

    @ApiModelProperty("需要弹窗")
    private Boolean popUp;

}
