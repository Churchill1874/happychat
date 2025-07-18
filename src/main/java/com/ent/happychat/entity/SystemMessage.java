package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.common.constant.enums.InfoEnum;
import com.ent.happychat.common.constant.enums.SystemNoticeEnum;
import com.ent.happychat.common.constant.enums.MessageTypeEnum;
import com.ent.happychat.entity.base.BaseInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("system_message")
@ApiModel("消息")
public class SystemMessage extends BaseInfo implements Serializable {
    private static final long serialVersionUID = 5655088707782304840L;


    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty("信息类型messageType是评论类型的时候,title是自己发表对新闻的评论信息,现在有人对此评论发表评论." +
        "信息类型messageType为系统类型的时候,title是系统消息标题")
    private String title;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("是否已经读取")
    private Boolean status;

    @ApiModelProperty("消息类型")
    private MessageTypeEnum messageType;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("收取人账号")
    private Long recipientId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("发送人账号")
    private Long senderId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("新闻id")
    private Long newsId;

    @ApiModelProperty("评论来源")
    private InfoEnum infoType;

    @ApiModelProperty("图片路径")
    private String imagePath;

    @ApiModelProperty("需要弹窗")
    private Boolean popup;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("评论id")
    private Long commentId;

    @ApiModelProperty("被回复评论的内容")
    private String comment;

    @ApiModelProperty("系统通知类型类型")
    private SystemNoticeEnum systemNoticeType;

}
