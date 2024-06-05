package com.ent.happychat.pojo.req.player;

import com.ent.happychat.common.constant.enums.LevelTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class PlayerInfoUpdateReq implements Serializable {
    private static final long serialVersionUID = -6961664538129832125L;

    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    @NotBlank(message = "昵称不能为空")
    @Length(min = 1, max = 15, message = "昵称长度请设置1-15位之间")
    @ApiModelProperty(value = "昵称", required = true)
    private String name;

    @ApiModelProperty(value = "手机号", required = false)
    private String phone;

    @ApiModelProperty(value = "邮箱", required = false)
    private String email;

    @ApiModelProperty(value = "性别 1男 0女", required = false)
    private String gender;

    @ApiModelProperty(value = "城市", required = false)
    private String city;

    @ApiModelProperty(value = "生日", required = false)
    private LocalDate birth;

    @ApiModelProperty(value = "等级", required = false)
    private LevelTypeEnum level;

    @ApiModelProperty(value = "自我介绍", required = false)
    private String selfIntroduction;

    @ApiModelProperty(value = "头像路径", required = false)
    private String avatarPath;

}
