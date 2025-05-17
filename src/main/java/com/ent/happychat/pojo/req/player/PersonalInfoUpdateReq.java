package com.ent.happychat.pojo.req.player;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class PersonalInfoUpdateReq implements Serializable {
    private static final long serialVersionUID = 6907451855627573660L;

    @NotBlank(message = "请输入昵称")
    @Length(min = 1, max = 15, message = "昵称长度请设置1-15位之间")
    @ApiModelProperty(value = "昵称", required = true)
    private String name;

    @ApiModelProperty(value = "手机号", required = false)
    private String phone;

    @ApiModelProperty(value = "邮箱", required = false)
    private String email;

    @ApiModelProperty(value = "自我介绍", required = false)
    private String selfIntroduction;

    @ApiModelProperty(value = "城市", required = false)
    private String city;

    @NotBlank(message = "请指定头像")
    @ApiModelProperty(value = "头像路径", required = true)
    private String avatarPath;

    @ApiModelProperty("小飞机")
    private String tg;

    @ApiModelProperty(value = "生日", required = true)
    private LocalDate birth;

}
