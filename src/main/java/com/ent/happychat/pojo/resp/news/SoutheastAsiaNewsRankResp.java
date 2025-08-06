package com.ent.happychat.pojo.resp.news;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SoutheastAsiaNewsRankResp implements Serializable {
    private static final long serialVersionUID = -5283671837540465219L;
    private String southeastAsiaTitle1;
    private Integer southeastAsiaView1;
    private Integer southeastAsiaCount1;
    private LocalDateTime southeastAsiaTime1;
    private String southeastAsiaCountry1;

    private String southeastAsiaTitle2;
    private Integer southeastAsiaView2;
    private Integer southeastAsiaCount2;
    private LocalDateTime southeastAsiaTime2;
    private String southeastAsiaCountry2;

    private String southeastAsiaTitle3;
    private Integer southeastAsiaView3;
    private Integer southeastAsiaCount3;
    private LocalDateTime southeastAsiaTime3;
    private String southeastAsiaCountry3;
}
