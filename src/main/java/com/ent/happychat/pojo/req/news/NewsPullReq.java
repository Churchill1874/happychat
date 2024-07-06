package com.ent.happychat.pojo.req.news;

import com.ent.happychat.common.constant.enums.NewsCategoryEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class NewsPullReq implements Serializable {
    private static final long serialVersionUID = 4190992070618308387L;

    @NotNull(message = "拉取新闻类型不能为空")
    @ApiModelProperty("拉取的新闻类型")
    private List<NewsCategoryEnum> newsCategoryEnum;
}
