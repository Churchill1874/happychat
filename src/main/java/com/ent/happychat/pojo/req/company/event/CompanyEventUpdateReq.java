package com.ent.happychat.pojo.req.company.event;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class CompanyEventUpdateReq implements Serializable {
    private static final long serialVersionUID = -9092692381815913528L;

    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id", required = true)
    private Long id;
    @ApiModelProperty("图片 多个图片用逗号分割")
    private String image;
    @ApiModelProperty("详细描述")
    private String description;
    @ApiModelProperty("事件时间")
    private LocalDate eventDate;
}
