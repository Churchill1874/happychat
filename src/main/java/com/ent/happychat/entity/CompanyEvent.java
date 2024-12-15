package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.entity.base.BaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("公司评价")
@TableName("company_evaluate")
public class CompanyEvent extends BaseInfo implements Serializable {
    private static final long serialVersionUID = -4035066813991462249L;

    @ApiModelProperty("公司编码id")
    private Long companyId;
    @ApiModelProperty("图片 多个图片用逗号分割")
    private String image;
    @ApiModelProperty("详细描述")
    private String description;

}
