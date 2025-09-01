package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.common.constant.enums.CampEnum;
import com.ent.happychat.common.constant.enums.GenderEnum;
import com.ent.happychat.common.constant.enums.LevelEnum;
import com.ent.happychat.common.constant.enums.UserStatusEnum;
import com.ent.happychat.entity.base.UpdateBaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ApiModel("玩家")
@TableName("player_info")
public class PlayerInfo extends UpdateBaseInfo implements Serializable {
    private static final long serialVersionUID = 8299446727161357208L;
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

    @ApiModelProperty("性别")
    private GenderEnum gender;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("生日")
    private LocalDate birth;

    @ApiModelProperty("等级")
    private LevelEnum level;

    @ApiModelProperty("自我介绍")
    private String selfIntroduction;

    @ApiModelProperty("是否机器人")
    private Boolean isBot;

    @ApiModelProperty("状态")
    private UserStatusEnum status;

    @ApiModelProperty("头像路径")
    private String avatarPath;

    @ApiModelProperty("余额")
    private BigDecimal balance;

    @ApiModelProperty("盐")
    private String salt;

    @ApiModelProperty("小飞机")
    private String tg;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("ip")
    private String ip;

    @ApiModelProperty("阵营")
    private CampEnum campType;


}
