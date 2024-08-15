package com.ent.happychat.pojo.req.company;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
@Data
public class CompanyAddReq implements Serializable {
    private static final long serialVersionUID = -8125706716407639722L;

    @NotBlank(message = "名字不能为空")
    @ApiModelProperty(value = "名字", required = true)
    private String name;
    @NotBlank(message = "城市不能为空")
    @ApiModelProperty(value = "所在城市", required = true)
    private String city;
    @ApiModelProperty("公司图片地址")
    private String logo;
    @ApiModelProperty("描述")
    private String description;

}
