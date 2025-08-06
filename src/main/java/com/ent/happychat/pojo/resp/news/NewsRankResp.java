package com.ent.happychat.pojo.resp.news;

import lombok.Data;

import java.io.Serializable;
@Data
public class NewsRankResp implements Serializable {
    private static final long serialVersionUID = -8239743150750346628L;
    private String newsTitleTop;
    private String newsTitle1;
    private String newsTitle2;
    private String newsTitle3;
}
