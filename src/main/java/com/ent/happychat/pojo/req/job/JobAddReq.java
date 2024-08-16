package com.ent.happychat.pojo.req.job;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class JobAddReq implements Serializable {
    private static final long serialVersionUID = -7417153387377595238L;

    @NotBlank(message = "联系方式不能为空")
    @ApiModelProperty(value = "联系方式",required = true)
    private String contact;

    @NotBlank(message = "岗位名称不能为空")
    @Length(min = 1, max = 50, message = "岗位长度1-50")
    @ApiModelProperty(value = "岗位名称 多个可以用逗号分开",required = true)
    private String name;

    @NotBlank(message = "工作城市不能为空")
    @Length(min = 1, max = 20, message = "工作城市长度1-20")
    @ApiModelProperty(value = "所在城市 多个可以用逗号分开 远程",required = true)
    private String city;

    @ApiModelProperty("其他福利")
    private String welfare = "无";

    @NotBlank(message = "住宿环境不能为空")
    @Length(min = 1, max = 10, message = "住宿环境长度1-10")
    @ApiModelProperty(value = "住宿环境 单人间 双人间 多人间 不提供",required = true)
    private String room;

    @ApiModelProperty("外宿补贴 无 或 具体补贴金额")
    private String roomOut = "无";

    @ApiModelProperty("图片")
    private String image;

    @ApiModelProperty("学历要求条件 无 大专 本科 硕士")
    private String educationConditions = "无";

    @ApiModelProperty("年龄要求条件")
    private String ageConditions = "无";

    @NotBlank(message = "技能要求不能为空")
    @Length(min = 1, max = 1000, message = "技能要求长度1-1000")
    @ApiModelProperty("技能要求条件")
    private String skillConditions = "无";

    @NotBlank(message = "薪水范围不能为空")
    @Length(min = 1, max = 30, message = "薪水范围长度1-30")
    @ApiModelProperty(value = "薪水范围 格式 xx-xxx", required = true)
    private String salaryRange;

    @NotBlank(message = "休假方式不能为空")
    @Length(min = 1, max = 20, message = "休假方式长度1-20")
    @ApiModelProperty(value = "休假方式 单休 双休 大小周",required = true)
    private String holiday;

    @NotBlank(message = "公司名称不能为空")
    @Length(min = 1, max = 20, message = "公司名称长度1-20")
    @ApiModelProperty(value = "公司名称",required = true)
    private String companyName;

    @NotBlank(message = "公司简介不能为空")
    @Length(min = 1, max = 200, message = "公司简介1-200")
    @ApiModelProperty(value = "公司简介",required = true)
    private String companyEncapsulate;

    @NotBlank(message = "办公环境不能为空")
    @Length(min = 1, max = 10, message = "办公环境1-10")
    @ApiModelProperty(value = "办公环境 写字楼 公寓 别墅 远程",required = true)
    private String environment;

    @NotBlank(message = "团队规模不能为空")
    @Length(min = 1, max = 20, message = "团队规模长度1-20")
    @ApiModelProperty(value = "团队规模 0-20 20-50 50-100 100+",required = true)
    private String teamScale;

    @ApiModelProperty("标签 用逗号隔开 可多选内容 老公司,氛围好,领导nice,休假多,龙头公司,高绩效,团建丰富")
    private String tag;

    @NotBlank(message = "年假不能为空")
    @Length(min=1, max = 20, message = "年假长度1-20")
    @ApiModelProperty(value = "年假",required = true)
    private String annualLeave;

    @NotBlank(message = "项目不能为空")
    @Length(min=1, max = 20, message = "项目长度1-20")
    @ApiModelProperty("经营项目")
    private String project;

}
