package com.ent.happychat.controller.player;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.constant.CacheKeyConstant;
import com.ent.happychat.common.constant.enums.NewsStatusEnum;
import com.ent.happychat.common.tools.HttpTools;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.News;
import com.ent.happychat.pojo.req.IdBase;
import com.ent.happychat.pojo.req.news.NewsPageReq;
import com.ent.happychat.pojo.resp.BooleanResp;
import com.ent.happychat.pojo.resp.news.HomeNewsResp;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.service.EhcacheService;
import com.ent.happychat.service.NewsService;
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
import java.util.List;

@Slf4j
@RestController
@Api(tags = "新闻")
@RequestMapping("/player/news")
public class NewsApi {

    @Autowired
    private NewsService newsService;
    @Autowired
    private EhcacheService ehcacheService;


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

    @PostMapping("/increaseLikesCount")
    @ApiOperation(value = "点赞新闻", notes = "点赞新闻")
    public R<BooleanResp> increaseLikesCount(@RequestBody @Valid IdBase req) {
        BooleanResp booleanResp = new BooleanResp();
        booleanResp.setValue(newsService.increaseLikesCount(req.getId()));
        return R.ok(booleanResp);
    }

}
