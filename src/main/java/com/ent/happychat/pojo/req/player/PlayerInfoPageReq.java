package com.ent.happychat.pojo.req.player;

import com.ent.happychat.common.constant.enums.LevelEnum;
import com.ent.happychat.common.constant.enums.UserStatusEnum;
import com.ent.happychat.pojo.req.PageBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class PlayerInfoPageReq extends PageBase implements Serializable {
    private static final long serialVersionUID = 5135728727206679950L;

    @ApiModelProperty("昵称")
    private String name;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("性别 1男 0女")
    private String gender;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("生日")
    private LocalDate birth;

    @ApiModelProperty("等级")
    private LevelEnum level;

    @ApiModelProperty("是否机器人")
    private Boolean isBot;

    @ApiModelProperty("状态")
    private UserStatusEnum status;

}
