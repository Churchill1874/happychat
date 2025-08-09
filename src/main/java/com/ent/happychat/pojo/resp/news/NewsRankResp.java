package com.ent.happychat.pojo.resp.news;

import lombok.Data;

import java.io.Serializable;
@Data
public class NewsRankResp implements Serializable {
    private static final long serialVersionUID = -8239743150750346628L;
    private Long newsTopId;
    private String newsTitleTop;
    private Long news1Id;
    private String newsTitle1;
    private Long news2Id;
    private String newsTitle2;
    private Long news3Id;
    private String newsTitle3;
}
