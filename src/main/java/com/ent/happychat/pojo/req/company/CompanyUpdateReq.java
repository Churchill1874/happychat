package com.ent.happychat.pojo.req.company;

import com.ent.happychat.pojo.req.IdBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class CompanyUpdateReq extends IdBase implements Serializable {
    private static final long serialVersionUID = 7726715529833464478L;

    @NotBlank(message = "名字不能为空")
    @ApiModelProperty("名字")
    private String name;
    @NotBlank(message = "所在城市不能为空")
    @ApiModelProperty("所在城市")
    private String city;
    @ApiModelProperty("公司图片地址")
    private String image;
    @ApiModelProperty("描述")
    private String description;
    @NotBlank(message = "团队规模不能为空")
    @ApiModelProperty("团队规模")
    private String teamScale;
    @ApiModelProperty("休假制度")
    private String holiday;
    @NotBlank(message = "薪资范围不能为空")
    @ApiModelProperty("薪资范围")
    private String salaryRange;
    @ApiModelProperty("领导性格")
    private String leadershipCharacter;
    @NotBlank(message = "居住不能为空")
    @ApiModelProperty("居住")
    private String live;
    @ApiModelProperty("办公环境")
    private String officeEnvironment;
    @ApiModelProperty("加班补偿")
    private String overtimeCompensation;
    @ApiModelProperty("奖金制度")
    private String bonus;
/*    @NotBlank(message = "评价不能为空")
    @ApiModelProperty(value="评价", required = true)
    private String evaluate;*/

}
