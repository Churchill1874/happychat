package com.ent.happychat.pojo.resp.news;

import lombok.Data;

import java.io.Serializable;
@Data
public class BannerResp implements Serializable {
    private static final long serialVersionUID = 8114190034947034040L;

    private String imagePath;

    private String title;

}
