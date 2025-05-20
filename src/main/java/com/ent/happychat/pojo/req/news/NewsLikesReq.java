package com.ent.happychat.pojo.req.news;

import com.ent.happychat.common.constant.enums.InfoEnum;
import com.ent.happychat.pojo.req.IdBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class NewsLikesReq extends IdBase implements Serializable {
    private static final long serialVersionUID = 1194122050761369554L;

    @NotNull(message = "新闻类型不能为空")
    @ApiModelProperty("新闻类型")
    private InfoEnum infoType;

}
