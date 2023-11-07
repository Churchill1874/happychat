package com.ent.happychat.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlayerToken {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("名字")
    private String name;

    @ApiModelProperty("登陆时间")
    private LocalDateTime loginTime;

    @ApiModelProperty("等级")
    private Integer level;

    @ApiModelProperty("手机号")
    private String phone;


}
