package com.ent.happychat.pojo.resp.vote;

import com.ent.happychat.entity.Vote;
import com.ent.happychat.entity.VoteOption;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class VoteResp extends Vote implements Serializable {
    private static final long serialVersionUID = -5513627213990722414L;

    @ApiModelProperty("是否已截止")
    private Boolean ended;

    @ApiModelProperty("选项列表，按 sort 排序")
    private List<VoteOption> options;

    @JsonSerialize(contentUsing = ToStringSerializer.class)
    @ApiModelProperty("当前登录用户已选的选项id，未登录或未投票为空")
    private List<Long> myOptionIds;

}
