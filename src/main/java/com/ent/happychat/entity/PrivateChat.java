package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("private_chat")
@ApiModel("私信聊天")
public class PrivateChat implements Serializable {
    private static final long serialVersionUID = 3928476344494927484L;

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("发送人账号")
    private String sendAccount;

    @ApiModelProperty("接收人账号")
    private String receiveAccount;

    @ApiModelProperty("私信内容")
    private String content;

    @ApiModelProperty("是否已经读取")
    private Boolean status;

}
