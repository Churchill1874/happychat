package com.ent.happychat.pojo.req.website;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class LoginPlayerReq implements Serializable {

    private static final long serialVersionUID = -8971765511064660846L;

    @ApiModelProperty(value = "账号 手机和账号登录使用其中一个即可", required = false)
    private Integer account;

    @ApiModelProperty(value = "网名 网名和账号登录使用其中一个即可", required = false)
    private String name;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码", required = true)
    private String password;

    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty(value = "验证码", required = true)
    private String verificationCode;

}
