package com.ent.happychat.pojo.resp.report;

import lombok.Data;

import java.util.List;
/**用户排行**/
@Data
public class RankReportResp {

    private List<RankInfoResp> registerList;

    private List<RankInfoResp> loginList;

    private List<RankInfoResp> commentList;

}
