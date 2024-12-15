package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.entity.base.BaseInfo;
import com.ent.happychat.entity.base.UpdateBaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("公司")
@TableName("company")
public class Company extends UpdateBaseInfo implements Serializable {
    private static final long serialVersionUID = 1461838478480641780L;

    @ApiModelProperty("名字")
    private String name;
    @ApiModelProperty("所在城市")
    private String city;
    @ApiModelProperty("公司图片地址")
    private String image;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("团队规模")
    private String teamScale;
    @ApiModelProperty("休假制度")
    private String holiday;
    @ApiModelProperty("薪资范围")
    private String salaryRange;
    @ApiModelProperty("领导性格")
    private String leadershipCharacter;
    @ApiModelProperty("居住")
    private String live;
    @ApiModelProperty("办公环境")
    private String officeEnvironment;
    @ApiModelProperty("加班补偿")
    private String overtimeCompensation;
    @ApiModelProperty("奖金制度")
    private String bonus;

}
