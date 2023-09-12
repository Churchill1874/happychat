package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.config.BigDecimalSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("user")
@ApiModel("用户类")
public class User extends BaseInfo implements Serializable {

    private static final long serialVersionUID = -2092696757443231064L;

    @TableField("name")
    @ApiModelProperty("昵称")
    private String name;

    @TableField("account")
    @ApiModelProperty("账号")
    private Integer account;

    @JsonIgnore
    @TableField("password")
    @ApiModelProperty("密码")
    private String password;

    @TableField("phone_number")
    @ApiModelProperty("手机号")
    private String phoneNumber;

    @TableField("real_name")
    @ApiModelProperty("真实姓名")
    private String realName;

    @TableField("balance")
    @ApiModelProperty("余额")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal balance;

    @TableField("level")
    @ApiModelProperty("等级")
    private Integer level;

    @TableField("address")
    @ApiModelProperty("注册地址")
    private String address;

    @TableField("status")
    @ApiModelProperty("状态 1正常 0禁用")
    private Integer status;

    @TableField("avatar")
    @ApiModelProperty("头像")
    private Integer avatar;

    @TableField("role")
    @ApiModelProperty("角色")
    private Integer role;

    @TableField("platform")
    @ApiModelProperty("平台")
    private Integer platform;


}
