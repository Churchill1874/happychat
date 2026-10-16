package com.ent.happychat.pojo.req.vote;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class VoteAddReq implements Serializable {
    private static final long serialVersionUID = 2952418820379571370L;

    @NotBlank(message = "标题不能为空")
    @Length(max = 100, message = "标题最多100字")
    @ApiModelProperty(value = "标题", required = true)
    private String title;

    @ApiModelProperty("投票事件简介")
    private String description;

    @ApiModelProperty("封面图")
    private String coverImagePath;

    @ApiModelProperty("是否多选，默认单选")
    private Boolean multiSelect;

    @NotNull(message = "统计截止时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "统计截止时间", required = true)
    private LocalDateTime deadline;

    @ApiModelProperty("是否置顶")
    private Boolean isTop;

    @ApiModelProperty("展示状态")
    private Boolean status;

    @Valid
    @NotNull(message = "投票选项不能为空")
    @Size(min = 2, max = 5, message = "投票选项至少2个，最多5个")
    @ApiModelProperty(value = "投票选项 2~5个，按顺序排列", required = true)
    private List<VoteOptionReq> options;

}
