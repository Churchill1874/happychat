package com.ent.happychat.pojo.req.company.event;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class CompanyEventAddReq implements Serializable {
    private static final long serialVersionUID = 3675273782213659432L;

    @NotNull(message = "公司id不能为空")
    @ApiModelProperty(value = "公司id", required = true)
    private Long companyId;
    @NotBlank(message = "简述不能为空")
    @ApiModelProperty(value = "简述", required = true)
    private String encapsulate;
    @ApiModelProperty("图片 多个图片用逗号分割")
    private String image;
    @ApiModelProperty("详细描述")
    private String description;

}
