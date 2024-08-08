package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.common.constant.enums.ManageRoleEnum;
import com.ent.happychat.entity.base.BaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("管理员")
@TableName("administrators")
public class Administrators extends BaseInfo implements Serializable {
    private static final long serialVersionUID = -7719439849954352417L;

    @ApiModelProperty("角色")
    private ManageRoleEnum role;

    @ApiModelProperty("盐值")
    private String salt;

    @TableField(value = "password", strategy = FieldStrategy.NOT_EMPTY)
    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("名字")
    private String name;

    @ApiModelProperty("账号")
    private String account;


}
