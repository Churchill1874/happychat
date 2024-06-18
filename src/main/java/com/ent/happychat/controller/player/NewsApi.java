package com.ent.happychat.controller.player;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.constant.CacheKeyConstant;
import com.ent.happychat.common.constant.enums.NewsStatusEnum;
import com.ent.happychat.entity.News;
import com.ent.happychat.pojo.req.news.NewsPage;
import com.ent.happychat.pojo.resp.news.HomeNews;
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

    @PostMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<IPage<News>> page(@RequestBody @Valid NewsPage req) {
        IPage<News> iPage = newsService.queryPage(req);
        return R.ok(iPage);
    }

    @PostMapping("/homeNews")
    @ApiOperation(value = "首页新闻 置顶和前几十排名新闻", notes = "首页新闻 置顶和前几十排名新闻")
    public R<HomeNews> headNews() {
        Cache<String, HomeNews> cache = ehcacheService.homeNewsCache();
        HomeNews homeNews = cache.get(CacheKeyConstant.HOME_NEWS_KEY);
        if (homeNews != null){
            return R.ok(homeNews);
        }

        homeNews = new HomeNews();

        //获取1条最近置顶新闻
        List<News> topList = newsService.findByNewsStatus(NewsStatusEnum.TOP, 1);
        if (CollectionUtils.isNotEmpty(topList)) {
            homeNews.setTopNews(topList.get(0));
        }

        //获取排名前十新闻
        NewsPage newsPage = new NewsPage();
        newsPage.setPageNum(1);
        newsPage.setPageSize(10);
        IPage<News> iPage = newsService.queryPage(newsPage);
        homeNews.setNewsList(iPage.getRecords());

        cache.put(CacheKeyConstant.HOME_NEWS_KEY, homeNews);
        return R.ok(homeNews);
    }


}
