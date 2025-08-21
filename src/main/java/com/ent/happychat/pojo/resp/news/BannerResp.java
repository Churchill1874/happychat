package com.ent.happychat.pojo.resp.news;

import com.ent.happychat.common.constant.enums.ImageTypeEnum;
import com.ent.happychat.common.constant.enums.InfoEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class BannerResp implements Serializable {
    private static final long serialVersionUID = 8114190034947034040L;

    private String imagePath;

    private String title;

    private ImageTypeEnum imageType;

    private InfoEnum newsType;

    private Long newsId;

}
