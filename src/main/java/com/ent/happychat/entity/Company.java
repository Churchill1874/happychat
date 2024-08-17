package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.entity.base.BaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("公司")
@TableName("company")
public class Company extends BaseInfo implements Serializable {
    private static final long serialVersionUID = 1461838478480641780L;

    @ApiModelProperty("名字")
    private String name;
    @ApiModelProperty("所在城市")
    private String city;
    @ApiModelProperty("公司图片地址")
    private String logo;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("团队规模")
    private String teamScale;
    @ApiModelProperty("休假制度")
    private String holiday;
    @ApiModelProperty("评价")
    private String evaluate;
}
