package com.ent.happychat.pojo.req.lottery.dealer;

import com.ent.happychat.common.constant.enums.LotteryStatusEnum;
import com.ent.happychat.common.constant.enums.LotteryTypeEnum;
import com.ent.happychat.pojo.req.PageBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class DealerPageReq extends PageBase implements Serializable {
    private static final long serialVersionUID = -398723881634974903L;

    @NotNull(message = "彩种类型不能为空")
    @ApiModelProperty("彩种类型")
    private LotteryTypeEnum type;
    @ApiModelProperty("彩票状态")
    private LotteryStatusEnum status;

}
