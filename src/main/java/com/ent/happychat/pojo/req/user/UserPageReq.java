package com.ent.happychat.pojo.req.user;

import com.ent.happychat.pojo.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserPageReq extends Page implements Serializable {

    private static final long serialVersionUID = -4795834215061216103L;

    @ApiModelProperty("昵称")
    private String name;

    @ApiModelProperty("账号")
    private Integer account;

    @ApiModelProperty("手机号")
    private String phoneNumber;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("等级")
    private Integer level;

    @ApiModelProperty("注册地址")
    private String address;

    @ApiModelProperty("状态 1正常 0禁用")
    private Integer status;

    @ApiModelProperty("角色 0机器人,1玩家,2管理员,3超级管理员")
    private Integer role;

    @ApiModelProperty("平台")
    private Integer platform;


}
