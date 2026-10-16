package com.ent.happychat.pojo.req.vote;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class VoteOptionReq implements Serializable {
    private static final long serialVersionUID = -4419617326226049871L;

    @ApiModelProperty("选项id，修改时传入已有选项id，新增选项不传")
    private Long id;

    @NotBlank(message = "选项内容不能为空")
    @Length(max = 100, message = "选项内容最多100字")
    @ApiModelProperty(value = "选项内容", required = true)
    private String content;

    @ApiModelProperty("选项图片")
    private String imagePath;

}
