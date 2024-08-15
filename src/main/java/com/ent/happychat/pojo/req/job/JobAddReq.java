package com.ent.happychat.pojo.req.job;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class JobAddReq implements Serializable {
    private static final long serialVersionUID = -7417153387377595238L;

    @NotBlank(message = "岗位名称不能为空")
    @ApiModelProperty("岗位名称 多个可以用逗号分开")
    private String name;

    @NotBlank(message = "工作城市不能为空")
    @ApiModelProperty("所在城市 多个可以用逗号分开 远程")
    private String city;

    @ApiModelProperty("其他福利")
    private String welfare = "无";

    @NotBlank(message = "住宿环境不能为空")
    @ApiModelProperty("住宿环境 单人间 双人间 多人间 不提供")
    private String room;

    @ApiModelProperty("外宿补贴 无 或 具体补贴金额")
    private String roomOut = "无";

    @ApiModelProperty("图片")
    private String image;

    @ApiModelProperty("学历要求条件 无 大专 本科 硕士")
    private String educationConditions = "无";

    @ApiModelProperty("年龄要求条件")
    private String ageConditions = "无";

    @ApiModelProperty("技能要求条件")
    private String skillConditions = "无";

    @NotBlank(message = "薪水上限不能为空")
    @ApiModelProperty("薪水上限")
    private String salaryUp;

    @NotBlank(message = "薪水下限不能为空")
    @ApiModelProperty("薪水下限")
    private String salaryDown;

    @NotBlank(message = "休假方式不能为空")
    @ApiModelProperty("休假方式 单休 双休 大小周")
    private String holiday;

    @NotBlank(message = "公司名称不能为空")
    @ApiModelProperty("公司名称")
    private String companyName;

    @NotBlank(message = "公司简介不能为空")
    @ApiModelProperty("公司简介")
    private String companyEncapsulate;

    @NotBlank(message = "办公环境不能为空")
    @ApiModelProperty("办公环境 写字楼 公寓 别墅 远程")
    private String environment;

    @NotBlank(message = "团队规模不能为空")
    @ApiModelProperty("团队规模 0-20 20-50 50-100 100+")
    private String teemScale;

}