package com.ent.happychat.pojo.req.promotion;

import com.ent.happychat.common.constant.enums.PromotionTypeEnum;
import com.ent.happychat.pojo.req.PageBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PromotionPageReq extends PageBase implements Serializable {
    private static final long serialVersionUID = 8526795146708710302L;

    @ApiModelProperty("记录id")
    private Long id;

    @ApiModelProperty("新闻来源")
    private PromotionTypeEnum type;

    @ApiModelProperty("是否置顶")
    private Boolean isTop;

    @ApiModelProperty("联系方式")
    private String contact;

    @ApiModelProperty("区域")
    private String area;

    @ApiModelProperty("展示状态")
    private Boolean status;

}
