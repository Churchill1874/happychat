package com.ent.happychat.pojo.req.vote;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 修改投票
 * options 中带 id 的为修改已有选项，不带 id 的为新增选项，原有但未传入的选项会被删除（连同其投票记录）
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VoteUpdateReq extends VoteAddReq implements Serializable {
    private static final long serialVersionUID = -1757240040785127066L;

    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id", required = true)
    private Long id;

}
