package com.ent.happychat.pojo.req.player;

import com.ent.happychat.common.constant.enums.GenderEnum;
import com.ent.happychat.common.constant.enums.LevelEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class PlayerInfoAddReq implements Serializable {
    private static final long serialVersionUID = 7444985824789126569L;

    @NotBlank(message = "昵称不能为空")
    @Length(min = 1, max = 15, message = "昵称长度请设置1-15位之间")
    @ApiModelProperty(value = "昵称", required = true)
    private String name;

    @NotBlank(message = "账号不能为空")
    @Length(min = 4, max = 20, message = "账号长度请设置4-20位之间")
    @ApiModelProperty(value = "账号", required = true)
    private String account;

    @NotBlank(message = "密码不能为空")
    @Length(min = 8, max = 20, message = "密码长度请设置8-20位之间")
    @ApiModelProperty(value = "密码", required = true)
    private String password;

    @ApiModelProperty(value = "手机号", required = false)
    private String phone;

    @ApiModelProperty(value = "邮箱", required = false)
    private String email;

    @ApiModelProperty(value = "性别 1男 0女", required = false)
    private GenderEnum gender;

    @ApiModelProperty(value = "城市", required = false)
    private String city;

    @ApiModelProperty(value = "生日", required = false)
    private LocalDate birth;

    @ApiModelProperty(value = "等级", required = false)
    private LevelEnum level;

    @ApiModelProperty(value = "自我介绍", required = false)
    private String selfIntroduction;

    @ApiModelProperty(value = "是否机器人 不传默认为是", required = false)
    private Boolean isBot;

    @ApiModelProperty(value = "头像路径", required = false)
    private String avatarPath;

}
