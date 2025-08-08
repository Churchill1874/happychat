package com.ent.happychat.controller.player;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ent.happychat.common.constant.CacheKeyConstant;
import com.ent.happychat.common.constant.enums.ImageTypeEnum;
import com.ent.happychat.common.constant.enums.NewsStatusEnum;
import com.ent.happychat.common.tools.HttpTools;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.*;
import com.ent.happychat.pojo.req.IdBase;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.pojo.req.image.ImageConfigPageReq;
import com.ent.happychat.pojo.req.news.NewsPageReq;
import com.ent.happychat.pojo.req.southeastasia.SoutheastAsiaPageReq;
import com.ent.happychat.pojo.resp.company.CompanyResp;
import com.ent.happychat.pojo.resp.news.*;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Api(tags = "新闻")
@RequestMapping("/player/news")
public class NewsApi {

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

    @PostMapping("/find")
    @ApiOperation(value = "新闻详情", notes = "新闻详情")
    public R<News> find(@RequestBody @Valid IdBase req) {
        PlayerTokenResp playerTokenResp = TokenTools.getPlayerToken(false);
        Long playerId = null;
        String playerName = null;
        if (playerTokenResp != null) {
            playerId = playerTokenResp.getId();
            playerName = playerTokenResp.getName();
        }
        News news = newsService.findByIdAndInsertRecord(HttpTools.getIp(), req.getId(), playerId, playerName);
        return R.ok(news);
    }

    @PostMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<IPage<News>> page(@RequestBody @Valid NewsPageReq req) {
        IPage<News> iPage = newsService.queryPage(req);
        return R.ok(iPage);
    }

/*
    @PostMapping("/homeNews")
    @ApiOperation(value = "首页新闻 置顶和前几十排名新闻", notes = "首页新闻 置顶和前几十排名新闻")
    public R<HomeNewsResp> headNews() {
        Cache<String, HomeNewsResp> cache = ehcacheService.homeNewsCache();
        HomeNewsResp homeNewsResp = cache.get(CacheKeyConstant.HOME_NEWS_KEY);
        if (homeNewsResp != null) {
            return R.ok(homeNewsResp);
        }

        homeNewsResp = new HomeNewsResp();

        //获取1条最近置顶新闻
        List<News> topList = newsService.findByNewsStatus(NewsStatusEnum.TOP, 1);
        if (CollectionUtils.isNotEmpty(topList)) {
            homeNewsResp.setTopNews(topList.get(0));
        }

        NewsPageReq newsPageReq = new NewsPageReq();
        newsPageReq.setPageNum(1);
        newsPageReq.setPageSize(10);
        newsPageReq.setViewSort(true);
        newsPageReq.setCommentsSort(true);
        IPage<News> iPage = newsService.queryPage(newsPageReq);
        homeNewsResp.setNewsList(iPage.getRecords());

        //获取随机的在线人数
        homeNewsResp.setOnlinePlayerCount(TokenTools.onlineCountRandom());

        //更新缓存
        cache.put(CacheKeyConstant.HOME_NEWS_KEY, homeNewsResp);
        return R.ok(homeNewsResp);
    }
*/

    @PostMapping("/home")
    @ApiOperation(value = "首页新闻 置顶和前几十排名新闻", notes = "首页新闻 置顶和前几十排名新闻")
    public R<HomeResp> home() {
        Cache<String, HomeResp> cache = ehcacheService.homeCache();
        HomeResp homeResp = cache.get(CacheKeyConstant.HOME_DATA);
        if(homeResp != null){
            return R.ok(homeResp);
        }

        homeResp = new HomeResp();
        getNewsRank(homeResp);
        getBannerList(homeResp);
        getCompany(homeResp);
        getHotLottery(homeResp);
        getSoutheastAsiaNews(homeResp);
        //获取随机的在线人数
        homeResp.setOnlineCount(TokenTools.onlineCountRandom());

        //更新缓存
        cache.put(CacheKeyConstant.HOME_DATA, homeResp);
        return R.ok(homeResp);
    }

    private void getNewsRank(HomeResp homeResp){
        //获取1条最近置顶新闻
        NewsRankResp newsRankResp = null;
        List<News> topList = newsService.findByNewsStatus(NewsStatusEnum.TOP, 1);
        if (CollectionUtils.isNotEmpty(topList)) {
            newsRankResp = new NewsRankResp();
            newsRankResp.setNewsTitleTop(topList.get(0).getTitle());
        }

        NewsPageReq newsPageReq = new NewsPageReq();
        newsPageReq.setPageNum(1);
        newsPageReq.setPageSize(3);
        newsPageReq.setViewSort(true);
        newsPageReq.setCommentsSort(true);
        IPage<News> iPage = newsService.queryPage(newsPageReq);
        if (CollectionUtils.isNotEmpty(iPage.getRecords())){
            if (newsRankResp == null){
                newsRankResp = new NewsRankResp();
            }
            for(int i=0; i<iPage.getRecords().size(); i++){
                News news = iPage.getRecords().get(i);
                if( i == 0 ){
                    newsRankResp.setNewsTitle1(news.getTitle());
                }
                if( i == 1 ){
                    newsRankResp.setNewsTitle2(news.getTitle());
                }
                if( i == 2 ){
                    newsRankResp.setNewsTitle3(news.getTitle());
                }
            }
        }
        homeResp.setNewsRank(newsRankResp);
    }

    private void getBannerList(HomeResp homeResp){
        ImageConfigPageReq imageConfigPageReq = new ImageConfigPageReq();
        imageConfigPageReq.setPageNum(1);
        imageConfigPageReq.setPageSize(3);
        imageConfigPageReq.setImageType(ImageTypeEnum.BANNER);
        imageConfigPageReq.setStatus(true);
        List<ImageConfig> imageConfigList = imageConfigService.queryPage(imageConfigPageReq).getRecords();
        if (CollectionUtils.isNotEmpty(imageConfigList)){
            List<BannerResp> list = new ArrayList<>();
            for(ImageConfig imageConfig: imageConfigList){
                BannerResp bannerResp = new BannerResp();
                bannerResp.setTitle(imageConfig.getDescription());
                bannerResp.setImagePath(imageConfig.getPath());
                list.add(bannerResp);
            }
            homeResp.setBannerList(list);
        }
    }

    private void getCompany(HomeResp homeResp){
        CompanyResp companyResp = companyService.last();
        if (companyResp != null){
            CompanyRankResp company = new CompanyRankResp();
            company.setCompanyAddress(companyResp.getCity());
            company.setCompanyName(companyResp.getName());
            company.setCompanyDescription(companyResp.getDescription());
            if(CollectionUtils.isNotEmpty(companyResp.getCompanyEventList())){
                if (companyResp.getCompanyEventList().get(0) != null){
                    CompanyEvent companyEvent1 = companyResp.getCompanyEventList().get(0);
                    company.setEventTime1(companyEvent1.getCreateTime());
                    company.setEventContent1(companyEvent1.getDescription());
                }
                if(companyResp.getCompanyEventList().get(1) != null){
                    CompanyEvent companyEvent2 = companyResp.getCompanyEventList().get(1);
                    company.setEventTime2(companyEvent2.getCreateTime());
                    company.setEventContent2(companyEvent2.getDescription());
                }
            }

            PageBase pageBase = new PageBase();
            pageBase.setPageNum(1);
            pageBase.setPageSize(5);
            IPage<Company> companyIPage = companyService.queryPage(pageBase);
            if (CollectionUtils.isNotEmpty(companyIPage.getRecords())){
                company.setCompanyNameList(companyIPage.getRecords().stream().map(Company::getName).collect(Collectors.toList()));
            }

            homeResp.setCompany(company);
        }


    }

    private void getHotLottery(HomeResp homeResp){
        PageBase pageBase = new PageBase();
        pageBase.setPageSize(1);
        pageBase.setPageNum(1);
        IPage<LotteryDealer> lotteryDealerIPage = lotteryDealerService.queryPage(pageBase);
        if(CollectionUtils.isNotEmpty(lotteryDealerIPage.getRecords())){
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

    private void getSoutheastAsiaNews(HomeResp homeResp){
        SoutheastAsiaPageReq southeastAsiaPageReq = new SoutheastAsiaPageReq();
        southeastAsiaPageReq.setPageNum(1);
        southeastAsiaPageReq.setPageSize(3);
        IPage<SoutheastAsia> southeastAsiaIPage = southeastAsiaService.queryPage(southeastAsiaPageReq);
        if(CollectionUtils.isNotEmpty(southeastAsiaIPage.getRecords())){
            SoutheastAsiaNewsRankResp southeastAsiaNewsRankResp = new SoutheastAsiaNewsRankResp();
            for(int i=0; i < southeastAsiaIPage.getRecords().size(); i++){
                SoutheastAsia southeastAsia = southeastAsiaIPage.getRecords().get(i);
                if(i == 0){
                    southeastAsiaNewsRankResp.setSoutheastAsiaTitle1(southeastAsia.getTitle());
                    southeastAsiaNewsRankResp.setSoutheastAsiaView1(southeastAsia.getViewCount());
                    southeastAsiaNewsRankResp.setSoutheastAsiaCount1(southeastAsia.getCommentsCount());
                    southeastAsiaNewsRankResp.setSoutheastAsiaTime1(southeastAsia.getCreateTime());
                    southeastAsiaNewsRankResp.setSoutheastAsiaCountry1(southeastAsia.getArea());
                }
                if(i == 1){
                    southeastAsiaNewsRankResp.setSoutheastAsiaTitle2(southeastAsia.getTitle());
                    southeastAsiaNewsRankResp.setSoutheastAsiaView2(southeastAsia.getViewCount());
                    southeastAsiaNewsRankResp.setSoutheastAsiaCount2(southeastAsia.getCommentsCount());
                    southeastAsiaNewsRankResp.setSoutheastAsiaTime2(southeastAsia.getCreateTime());
                    southeastAsiaNewsRankResp.setSoutheastAsiaCountry2(southeastAsia.getArea());
                }
                if(i == 2){
                    southeastAsiaNewsRankResp.setSoutheastAsiaTitle3(southeastAsia.getTitle());
                    southeastAsiaNewsRankResp.setSoutheastAsiaView3(southeastAsia.getViewCount());
                    southeastAsiaNewsRankResp.setSoutheastAsiaCount3(southeastAsia.getCommentsCount());
                    southeastAsiaNewsRankResp.setSoutheastAsiaTime3(southeastAsia.getCreateTime());
                    southeastAsiaNewsRankResp.setSoutheastAsiaCountry3(southeastAsia.getArea());
                }
            }
            homeResp.setSoutheastAsiaNewsRank(southeastAsiaNewsRankResp);
        }
    }

}
