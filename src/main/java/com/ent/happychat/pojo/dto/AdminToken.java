package com.ent.happychat.pojo.dto;

import com.ent.happychat.common.constant.enums.RoleEnum;
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

    @ApiModelProperty("角色")
    private RoleEnum role;

    @ApiModelProperty("tokenId")
    private String tokenId;

}
