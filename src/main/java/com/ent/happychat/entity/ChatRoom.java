package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.common.constant.enums.ChatContentEnum;
import com.ent.happychat.entity.base.BaseInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("chat_room")
public class ChatRoom implements Serializable {
    private static final long serialVersionUID = -6168896344604143482L;

    @TableId(value = "id",type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("聊天室号码")
    private Integer roomNumber;

    @ApiModelProperty("发送人id")
    private Long playerId;

    @ApiModelProperty("聊天内容")
    private String content;

    @ApiModelProperty("聊天内容类型")
    private ChatContentEnum type;

    @ApiModelProperty("是否是回复评论")
    private Boolean isReply;

    @ApiModelProperty("被回复玩家id")
    private Long targetPlayerId;

    @ApiModelProperty("回复的聊天id")
    private Long replyId;

    @ApiModelProperty("回复的内容")
    private String replyContent;

}
