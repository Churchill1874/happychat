package com.ent.happychat.pojo.req.politics;

import com.ent.happychat.common.constant.enums.NewsStatusEnum;
import com.ent.happychat.pojo.req.PageBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PoliticsPageReq extends PageBase {
    private static final long serialVersionUID = -1299428532174236746L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("新闻状态")
    private NewsStatusEnum newsStatus;

    @ApiModelProperty("国家")
    private String country;

    @ApiModelProperty("来源")
    private String source;

}
