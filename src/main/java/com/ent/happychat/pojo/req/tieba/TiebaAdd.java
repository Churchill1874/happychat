package com.ent.happychat.pojo.req.tieba;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class TiebaAdd implements Serializable {
    private static final long serialVersionUID = -4927255049409181248L;

    @ApiModelProperty("标题")
    @NotBlank(message = "标题不能为空")
    @Length(min = 1, max = 30, message = "请输入1-30字符长度的标题")
    private String title;
    @NotBlank(message = "内容不能为空")
    @Length(min = 1, max = 800, message = "请输入1-800字符长度的内容")
    @ApiModelProperty("内容")
    private String content;
    @ApiModelProperty("图片1")
    private String image1;
    @ApiModelProperty("图片2")
    private String image2;
    @ApiModelProperty("图片3")
    private String image3;
    @ApiModelProperty("图片4")
    private String image4;
    @ApiModelProperty("图片5")
    private String image5;
    @ApiModelProperty("图片6")
    private String image6;


}
