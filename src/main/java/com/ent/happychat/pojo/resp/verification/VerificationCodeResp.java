package com.ent.happychat.pojo.resp.verification;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class VerificationCodeResp implements Serializable {
    private static final long serialVersionUID = 298362008017450983L;

    @ApiModelProperty("验证码图片")
    private String captchaImage;

}
