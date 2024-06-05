package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.common.constant.enums.GenderEnum;
import com.ent.happychat.common.constant.enums.LevelTypeEnum;
import com.ent.happychat.common.constant.enums.UserStatusEnum;
import com.ent.happychat.entity.base.CreatorBaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@ApiModel("玩家")
@TableName("player_info")
public class PlayerInfo extends CreatorBaseInfo implements Serializable {
    private static final long serialVersionUID = 8299446727161357208L;
    @TableField("name")
    @ApiModelProperty("昵称")
    private String name;

    @TableField("account")
    @ApiModelProperty("账号")
    private String account;

    @TableField("password")
    @ApiModelProperty("密码")
    private String password;

    @TableField("phone")
    @ApiModelProperty("手机号")
    private String phone;

    @TableField("email")
    @ApiModelProperty("邮箱")
    private String email;

    @TableField("gender")
    @ApiModelProperty("性别")
    private GenderEnum gender;

    @TableField("city")
    @ApiModelProperty("城市")
    private String city;

    @TableField("birth")
    @ApiModelProperty("生日")
    private LocalDate birth;

    @TableField("level")
    @ApiModelProperty("等级")
    private LevelTypeEnum level;

    @TableField("self_introduction")
    @ApiModelProperty("自我介绍")
    private String selfIntroduction;

    @TableField("is_bot")
    @ApiModelProperty("是否机器人")
    private Boolean isBot;

    @TableField("status")
    @ApiModelProperty("状态")
    private UserStatusEnum status;

    @TableField("avatar_path")
    @ApiModelProperty("头像路径")
    private String avatarPath;

    @TableField("salt")
    @ApiModelProperty("盐")
    private String salt;

}
