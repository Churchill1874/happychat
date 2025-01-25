package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.entity.base.BaseInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("工作")
@TableName("job")
public class Job extends BaseInfo implements Serializable {
    private static final long serialVersionUID = -1021466672750849654L;

    @ApiModelProperty("联系方式")
    private String contact;

    @ApiModelProperty("岗位名称 多个可以用逗号分开")
    private String name;

    @ApiModelProperty("所在城市 多个可以用逗号分开")
    private String city;

    @ApiModelProperty("其他福利")
    private String welfare;

    @ApiModelProperty("住宿环境 单人间 双人间 多人间")
    private String room;

    @ApiModelProperty("外宿补贴 无 或 具体补贴金额")
    private String roomOut;

    @ApiModelProperty("图片")
    private String image;

    @ApiModelProperty("学历要求条件 无 大专 本科 硕士")
    private String educationConditions;

    @ApiModelProperty("年龄要求条件")
    private String ageConditions;

    @ApiModelProperty("技能要求条件")
    private String skillConditions;

    @ApiModelProperty("薪水上限")
    private String salaryRange;

    @ApiModelProperty("休假方式 单休 双休 大小周")
    private String holiday;

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("公司简介")
    private String companyEncapsulate;

    @ApiModelProperty("办公环境 写字楼 公寓 别墅 远程")
    private String environment;

    @ApiModelProperty("团队规模 0-20 20-50 50-100 100+")
    private String teamScale;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("最后一次 需要招聘 更新时间")
    private LocalDateTime lastTime;

    @ApiModelProperty("标签 用逗号隔开 可多选内容 老公司,氛围好,领导nice,休假多,龙头公司,高绩效,团建丰富")
    private String tag;

    @ApiModelProperty("年假")
    private String annualLeave;

    @ApiModelProperty("经营项目")
    private String project;

    private String abc1;

}
