package com.ent.happychat.pojo.resp.player;

import com.ent.happychat.common.constant.enums.GenderEnum;
import com.ent.happychat.common.constant.enums.LevelEnum;
import com.ent.happychat.common.constant.enums.UserStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PlayerInfoResp implements Serializable {

    private static final long serialVersionUID = -1657373368557831640L;

    @ApiModelProperty("玩家id")
    private Long id;
    @ApiModelProperty("昵称")
    private String name;
    @ApiModelProperty("账号")
    private String account;
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
    @ApiModelProperty("粉丝数量")
    private Long followersCount = 0L;
    @ApiModelProperty("关注数量")
    private Long collectCount = 0L;
    @ApiModelProperty("被点赞数量")
    private Long likesReceivedCount = 0L;
}
