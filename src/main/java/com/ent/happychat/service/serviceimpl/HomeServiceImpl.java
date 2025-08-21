package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ent.happychat.common.constant.CacheKeyConstant;
import com.ent.happychat.common.constant.enums.ImageTypeEnum;
import com.ent.happychat.common.constant.enums.LotteryTypeEnum;
import com.ent.happychat.common.constant.enums.NewsStatusEnum;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.*;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.pojo.req.betorder.BetOrderPageReq;
import com.ent.happychat.pojo.req.image.ImageConfigPageReq;
import com.ent.happychat.pojo.req.news.NewsPageReq;
import com.ent.happychat.pojo.req.promotion.PromotionPageReq;
import com.ent.happychat.pojo.req.southeastasia.SoutheastAsiaPageReq;
import com.ent.happychat.pojo.resp.company.CompanyResp;
import com.ent.happychat.pojo.resp.news.*;
import com.ent.happychat.service.*;
import org.ehcache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private NewsService newsService;
    @Autowired
    private EhcacheService ehcacheService;
    @Autowired
    private SoutheastAsiaService southeastAsiaService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private LotteryDealerService lotteryDealerService;
    @Autowired
    private PoliticsLotteryService politicsLotteryService;
    @Autowired
    private ImageConfigService imageConfigService;
    @Autowired
    private BetOrderService betOrderService;
    @Autowired
    private PoliticsService politicsService;
    @Autowired
    private PromotionService promotionService;

    @Override
    public HomeResp getHomeData() {
        Cache<String, HomeResp> cache = ehcacheService.homeCache();
        HomeResp homeResp = cache.get(CacheKeyConstant.HOME_DATA);
        if (homeResp != null) {
            return homeResp;
        }

        homeResp = new HomeResp();
        getNewsRank(homeResp);
        getBannerList(homeResp);
        getCompany(homeResp);
        getSoutheastAsiaNews(homeResp);
        //获取随机的在线人数
        homeResp.setOnlineCount(TokenTools.onlineCountRandom());
        getLast3BetOrderList(homeResp);
        getPolitics(homeResp);
        getPromotion(homeResp);
        //更新缓存
        cache.put(CacheKeyConstant.HOME_DATA, homeResp);
        return homeResp;
    }

    private void getPromotion(HomeResp homeResp){
        PromotionPageReq promotionPageReq = new PromotionPageReq();
        promotionPageReq.setPageNum(1);
        promotionPageReq.setPageSize(2);
        promotionPageReq.setStatus(true);
        List<Promotion> list = promotionService.queryPage(promotionPageReq).getRecords();
        PromotionResp promotionResp = null;
        if (CollectionUtils.isNotEmpty(list)){
            Promotion promotion1 = list.get(0);
            promotionResp = new PromotionResp();
            promotionResp.setTitle1(promotion1.getTitle());
            promotionResp.setVideoPath1(promotion1.getVideoPath());
            promotionResp.setVideoCover1(promotion1.getVideoCover());
            promotionResp.setImagePath1(promotion1.getImagePath());
            promotionResp.setArea1(promotion1.getArea());
            if(list.size() > 1){
                Promotion promotion2 = list.get(1);
                promotionResp.setArea2(promotion2.getArea());
                promotionResp.setTitle2(promotion2.getTitle());
                promotionResp.setVideoPath2(promotion2.getVideoPath());
                promotionResp.setVideoCover2(promotion2.getVideoCover());
                promotionResp.setImagePath2(promotion2.getImagePath());
            }
        }
        homeResp.setPromotion(promotionResp);
    }

    private void getPolitics(HomeResp homeResp){
        PageBase pageBase = new PageBase();
        pageBase.setPageNum(1);
        pageBase.setPageSize(3);
        List<Politics> list = politicsService.queryPage(pageBase).getRecords();
        homeResp.setPoliticsList(list);
    }


    private void getLast3BetOrderList(HomeResp homeResp) {
        BetOrderPageReq req = new BetOrderPageReq();
        req.setPageNum(1);
        req.setPageSize(3);
        req.setType(LotteryTypeEnum.POLITICS);
        List<BetOrder> list = betOrderService.queryPage(req).getRecords();
        homeResp.setBetOrderList(list);
    }

    private void getNewsRank(HomeResp homeResp) {
        //获取1条最近置顶新闻
        NewsRankResp newsRankResp = null;
        List<News> topList = newsService.findByNewsStatus(NewsStatusEnum.TOP, 1);
        if (CollectionUtils.isNotEmpty(topList)) {
            newsRankResp = new NewsRankResp();
            News news = topList.get(0);
            newsRankResp.setNewsTitleTop(news.getTitle());
            newsRankResp.setNewsTopId(news.getId());
        }

        NewsPageReq newsPageReq = new NewsPageReq();
        newsPageReq.setPageNum(1);
        newsPageReq.setPageSize(3);
        newsPageReq.setViewSort(true);
        newsPageReq.setCommentsSort(true);
        IPage<News> iPage = newsService.queryPage(newsPageReq);
        if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
            if (newsRankResp == null) {
                newsRankResp = new NewsRankResp();
            }
            for (int i = 0; i < iPage.getRecords().size(); i++) {
                News news = iPage.getRecords().get(i);
                if (i == 0) {
                    newsRankResp.setNews1Id(news.getId());
                    newsRankResp.setNewsTitle1(news.getTitle());
                }
                if (i == 1) {
                    newsRankResp.setNews2Id(news.getId());
                    newsRankResp.setNewsTitle2(news.getTitle());
                }
                if (i == 2) {
                    newsRankResp.setNews3Id(news.getId());
                    newsRankResp.setNewsTitle3(news.getTitle());
                }
            }
        }
        homeResp.setNewsRank(newsRankResp);
    }

    private void getBannerList(HomeResp homeResp) {
        QueryWrapper<ImageConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ImageConfig::getStatus, true);
        List<ImageConfig> imageConfigList = imageConfigService.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(imageConfigList)) {
            List<BannerResp> list = new ArrayList<>();
            for (ImageConfig imageConfig : imageConfigList) {
                BannerResp bannerResp = new BannerResp();
                bannerResp.setTitle(imageConfig.getDescription());
                bannerResp.setImagePath(imageConfig.getPath());
                bannerResp.setNewsType(imageConfig.getNewsType());
                bannerResp.setImageType(imageConfig.getImageType());
                bannerResp.setNewsId(imageConfig.getNewsId());
                list.add(bannerResp);
            }
            homeResp.setBannerList(list);
        }
    }

    private void getCompany(HomeResp homeResp) {
        CompanyResp companyResp = companyService.last();
        if (companyResp != null) {
            CompanyRankResp company = new CompanyRankResp();
            company.setCompanyAddress(companyResp.getCity());
            company.setCompanyName(companyResp.getName());
            company.setCompanyDescription(companyResp.getDescription());
            if (CollectionUtils.isNotEmpty(companyResp.getCompanyEventList())) {

                if (companyResp.getCompanyEventList().size() > 0) {
                    CompanyEvent companyEvent1 = companyResp.getCompanyEventList().get(0);
                    company.setEventTime1(companyEvent1.getCreateTime());
                    company.setEventContent1(companyEvent1.getDescription());
                }
                if (companyResp.getCompanyEventList().size() > 1) {
                    CompanyEvent companyEvent2 = companyResp.getCompanyEventList().get(1);
                    company.setEventTime2(companyEvent2.getCreateTime());
                    company.setEventContent2(companyEvent2.getDescription());
                }
            }

            PageBase pageBase = new PageBase();
            pageBase.setPageNum(1);
            pageBase.setPageSize(4);
            IPage<Company> companyIPage = companyService.queryPage(pageBase);
            if (CollectionUtils.isNotEmpty(companyIPage.getRecords())) {
                company.setCompanyNameList(companyIPage.getRecords().stream().map(Company::getName).collect(Collectors.toList()));
            }

            homeResp.setCompany(company);
        }


    }

    private void getHotLottery(HomeResp homeResp) {
        PageBase pageBase = new PageBase();
        pageBase.setPageSize(1);
        pageBase.setPageNum(1);
        IPage<LotteryDealer> lotteryDealerIPage = lotteryDealerService.queryPage(pageBase);
        if (CollectionUtils.isNotEmpty(lotteryDealerIPage.getRecords())) {
            LotteryDealer lotteryDealer = lotteryDealerIPage.getRecords().get(0);
            PoliticsLottery politicsLottery = politicsLotteryService.getById(lotteryDealer.getLotteryId());

            HotLotteryResp hotLotteryResp = new HotLotteryResp();
            hotLotteryResp.setLotteryTitle(politicsLottery.getTitle());
            hotLotteryResp.setBetIcon1(politicsLottery.getIcon1());
            hotLotteryResp.setBetName1(politicsLottery.getChoose1());
            hotLotteryResp.setBetAmount1(lotteryDealer.getBet1Amount());
            hotLotteryResp.setOdds1(lotteryDealer.getOdds1());
            hotLotteryResp.setDescription1(politicsLottery.getDescribe1());
            hotLotteryResp.setBetIcon2(politicsLottery.getIcon2());
            hotLotteryResp.setBetName2(politicsLottery.getChoose2());
            hotLotteryResp.setBetAmount2(lotteryDealer.getBet2Amount());
            hotLotteryResp.setOdds2(lotteryDealer.getOdds2());
            hotLotteryResp.setDescription2(politicsLottery.getDescribe2());
            hotLotteryResp.setBetIcon3(politicsLottery.getIcon3());
            hotLotteryResp.setBetName3(politicsLottery.getChoose3());
            hotLotteryResp.setBetAmount3(lotteryDealer.getBet3Amount());
            hotLotteryResp.setOdds3(lotteryDealer.getOdds3());
            hotLotteryResp.setDescription3(politicsLottery.getDescribe3());
            hotLotteryResp.setPrizePool(lotteryDealer.getPrizePool());
            hotLotteryResp.setRemainingPrizePool(lotteryDealer.getRemainingPrizePool());
            hotLotteryResp.setDrawTime(politicsLottery.getDrawTime());
            homeResp.setHotLottery(hotLotteryResp);
        }
    }

    private void getSoutheastAsiaNews(HomeResp homeResp) {
        SoutheastAsiaPageReq southeastAsiaPageReq = new SoutheastAsiaPageReq();
        southeastAsiaPageReq.setPageNum(1);
        southeastAsiaPageReq.setPageSize(5);
        IPage<SoutheastAsia> southeastAsiaIPage = southeastAsiaService.queryPage(southeastAsiaPageReq);
        if (CollectionUtils.isNotEmpty(southeastAsiaIPage.getRecords())) {
            SoutheastAsiaNewsRankResp southeastAsiaNewsRankResp = new SoutheastAsiaNewsRankResp();
            for (int i = 0; i < southeastAsiaIPage.getRecords().size(); i++) {
                SoutheastAsia southeastAsia = southeastAsiaIPage.getRecords().get(i);
                if (i == 0) {
                    southeastAsiaNewsRankResp.setSoutheastAsiaTitle1(southeastAsia.getTitle());
                    southeastAsiaNewsRankResp.setSoutheastAsiaView1(southeastAsia.getViewCount());
                    southeastAsiaNewsRankResp.setSoutheastAsiaCount1(southeastAsia.getCommentsCount());
                    southeastAsiaNewsRankResp.setSoutheastAsiaTime1(southeastAsia.getCreateTime());
                    southeastAsiaNewsRankResp.setSoutheastAsiaCountry1(southeastAsia.getArea());
                }
                if (i == 1) {
                    southeastAsiaNewsRankResp.setSoutheastAsiaTitle2(southeastAsia.getTitle());
                    southeastAsiaNewsRankResp.setSoutheastAsiaView2(southeastAsia.getViewCount());
                    southeastAsiaNewsRankResp.setSoutheastAsiaCount2(southeastAsia.getCommentsCount());
                    southeastAsiaNewsRankResp.setSoutheastAsiaTime2(southeastAsia.getCreateTime());
                    southeastAsiaNewsRankResp.setSoutheastAsiaCountry2(southeastAsia.getArea());
                }
                if (i == 2) {
                    southeastAsiaNewsRankResp.setSoutheastAsiaTitle3(southeastAsia.getTitle());
                    southeastAsiaNewsRankResp.setSoutheastAsiaView3(southeastAsia.getViewCount());
                    southeastAsiaNewsRankResp.setSoutheastAsiaCount3(southeastAsia.getCommentsCount());
                    southeastAsiaNewsRankResp.setSoutheastAsiaTime3(southeastAsia.getCreateTime());
                    southeastAsiaNewsRankResp.setSoutheastAsiaCountry3(southeastAsia.getArea());
                }
                if (i == 3) {
                    southeastAsiaNewsRankResp.setSoutheastAsiaTitle4(southeastAsia.getTitle());
                    southeastAsiaNewsRankResp.setSoutheastAsiaView4(southeastAsia.getViewCount());
                    southeastAsiaNewsRankResp.setSoutheastAsiaCount4(southeastAsia.getCommentsCount());
                    southeastAsiaNewsRankResp.setSoutheastAsiaTime4(southeastAsia.getCreateTime());
                    southeastAsiaNewsRankResp.setSoutheastAsiaCountry4(southeastAsia.getArea());
                }
                if (i == 4) {
                    southeastAsiaNewsRankResp.setSoutheastAsiaTitle5(southeastAsia.getTitle());
                    southeastAsiaNewsRankResp.setSoutheastAsiaView5(southeastAsia.getViewCount());
                    southeastAsiaNewsRankResp.setSoutheastAsiaCount5(southeastAsia.getCommentsCount());
                    southeastAsiaNewsRankResp.setSoutheastAsiaTime5(southeastAsia.getCreateTime());
                    southeastAsiaNewsRankResp.setSoutheastAsiaCountry5(southeastAsia.getArea());
                }
            }
            homeResp.setSoutheastAsiaNewsRank(southeastAsiaNewsRankResp);
        }
    }
}
