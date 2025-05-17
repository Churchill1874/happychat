package com.ent.happychat.pojo.resp.player;

import com.ent.happychat.common.constant.enums.GenderEnum;
import com.ent.happychat.common.constant.enums.LevelEnum;
import com.ent.happychat.common.constant.enums.UserStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PlayerInfoResp implements Serializable {
    private static final long serialVersionUID = -8594498471868687325L;

    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

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

    @ApiModelProperty("状态")
    private UserStatusEnum status;

    @ApiModelProperty("头像路径")
    private String avatarPath;

    @ApiModelProperty("余额")
    private BigDecimal balance;

    @ApiModelProperty("小飞机")
    private String tg;


}
