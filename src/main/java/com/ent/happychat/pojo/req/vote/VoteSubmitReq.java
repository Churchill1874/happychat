package com.ent.happychat.pojo.req.vote;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
public class VoteSubmitReq implements Serializable {
    private static final long serialVersionUID = 8811736052712925548L;

    @NotNull(message = "投票id不能为空")
    @ApiModelProperty(value = "投票id", required = true)
    private Long voteId;

    @NotEmpty(message = "请选择投票选项")
    @Size(max = 5, message = "选项数量错误")
    @ApiModelProperty(value = "选择的选项id，单选时只传1个", required = true)
    private List<Long> optionIds;

}
