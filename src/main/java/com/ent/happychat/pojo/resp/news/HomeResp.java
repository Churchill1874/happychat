package com.ent.happychat.pojo.resp.news;

import com.ent.happychat.entity.BetOrder;
import com.ent.happychat.entity.Politics;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class HomeResp implements Serializable {
    private static final long serialVersionUID = 2674731237761652183L;
    @ApiModelProperty("在线人数")
    private Integer onlineCount;
    @ApiModelProperty("新闻排行")
    private NewsRankResp newsRank;
    @ApiModelProperty("轮播图集合")
    private List<BannerResp> bannerList;
    @ApiModelProperty("追踪公司")
    private CompanyRankResp company;
    @ApiModelProperty("热门彩票")
    private HotLotteryResp hotLottery;
    @ApiModelProperty("东南亚新闻排名")
    private SoutheastAsiaNewsRankResp southeastAsiaNewsRank;
    @ApiModelProperty("最新下注记录")
    private List<BetOrder> betOrderList;
    @ApiModelProperty("政治新闻")
    private List<Politics> politicsList;
    @ApiModelProperty("推广信息")
    private PromotionResp promotion;

}
