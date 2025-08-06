package com.ent.happychat.pojo.resp.news;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CompanyRankResp implements Serializable {
    private static final long serialVersionUID = 2905645579052507696L;
    private String companyAddress;
    private String companyName;
    private String companyDescription;
    private LocalDateTime eventTime1;
    private String eventContent1;
    private LocalDateTime eventTime2;
    private String eventContent2;
    private List<String> companyNameList;
}
