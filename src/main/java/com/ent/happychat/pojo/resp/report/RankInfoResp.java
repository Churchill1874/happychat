package com.ent.happychat.pojo.resp.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RankInfoResp {

    private String name;

    private String avatarPath;

    private Long playerId;

    private String address;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    private Integer commentCount;

}
