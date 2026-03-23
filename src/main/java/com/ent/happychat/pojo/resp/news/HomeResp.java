package com.ent.happychat.pojo.resp.news;

import com.ent.happychat.entity.BetOrder;
import com.ent.happychat.entity.Exposure;
import com.ent.happychat.entity.Politics;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class HomeResp implements Serializable {
    private static final long serialVersionUID = 2674731237761652183L;
    @ApiModelProperty("在线人数")
    private Integer onlineCount = 0;
    @ApiModelProperty("新闻排行")
    private NewsRankResp newsRank = new NewsRankResp();
    @ApiModelProperty("轮播图集合")
    private List<BannerResp> bannerList = new ArrayList<>();
    @ApiModelProperty("追踪公司")
    private CompanyRankResp company = new CompanyRankResp();
    @ApiModelProperty("热门彩票")
    private HotLotteryResp hotLottery = new HotLotteryResp();
    @ApiModelProperty("东南亚新闻排名")
    private SoutheastAsiaNewsRankResp southeastAsiaNewsRank = new SoutheastAsiaNewsRankResp();
    @ApiModelProperty("最新下注记录")
    private List<BetOrder> betOrderList = new ArrayList<>();
    @ApiModelProperty("政治新闻")
    private List<Politics> politicsList = new ArrayList<>();
    @ApiModelProperty("推广信息")
    private PromotionResp promotion = new PromotionResp();
    @ApiModelProperty("曝光信息")
    private List<Exposure> exposureList = new ArrayList<>();

}
