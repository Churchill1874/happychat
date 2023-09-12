package com.ent.happychat.pojo.req.website;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class RegisterReq implements Serializable {

    private static final long serialVersionUID = 4105656196334674846L;

    @NotBlank(message = "网名昵称不能为空")
    @ApiModelProperty(value = "网名昵称", required = true)
    private String name;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码", required = true)
    private String password;

    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty(value = "验证码", required = true)
    private String verificationCode;

}
