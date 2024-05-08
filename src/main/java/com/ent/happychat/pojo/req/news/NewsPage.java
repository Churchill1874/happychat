package com.ent.happychat.pojo.req.news;

import com.ent.happychat.common.constant.enums.NewsCategoryEnum;
import com.ent.happychat.pojo.req.PageBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class NewsPage extends PageBase implements Serializable {
    private static final long serialVersionUID = -3968897108907540868L;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("新闻类别")
    private NewsCategoryEnum categoryEnum;

}
