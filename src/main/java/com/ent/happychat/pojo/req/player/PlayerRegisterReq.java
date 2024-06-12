package com.ent.happychat.pojo.req.player;

import com.ent.happychat.common.constant.enums.GenderEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@ApiModel("玩家注册信息")
public class PlayerRegisterReq implements Serializable {
    private static final long serialVersionUID = 3754040536204734634L;

    @NotBlank(message = "请输入昵称")
    @Length(max = 20, message = "请输入20位以内的昵称")
    @ApiModelProperty(value = "昵称", required = true)
    private String name;

    @NotBlank(message = "账号不能为空")
    @Length(min = 4, max = 20, message = "账号长度请设置4-20位之间")
    @ApiModelProperty(value = "账号", required = true)
    private String account;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @NotBlank(message = "请输入密码")
    @Length(max = 20, message = "请输入20位以内的密码")
    @ApiModelProperty(value = "密码", required = true)
    private String password;

    @ApiModelProperty(value = "生日", required = true)
    private LocalDate birth;

    @ApiModelProperty(value = "性别", required = true)
    private GenderEnum gender;

    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty(value = "验证码", required = true)
    private String verificationCode;

}
