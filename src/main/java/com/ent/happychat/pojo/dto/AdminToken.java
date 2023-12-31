package com.ent.happychat.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminToken {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("名字")
    private String name;

    @ApiModelProperty("登陆时间")
    private LocalDateTime loginTime;

}
