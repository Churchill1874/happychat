package com.ent.happychat.controller.player;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.constant.enums.NewsStatusEnum;
import com.ent.happychat.entity.News;
import com.ent.happychat.pojo.req.news.NewsPageReq;
import com.ent.happychat.pojo.resp.news.HeadNewsResp;
import com.ent.happychat.service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "新闻")
@RequestMapping("/player/news")
public class NewsApi {

    @Autowired
    private NewsService newsService;

    @PostMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<IPage<News>> page(@RequestBody @Valid NewsPageReq req) {
        IPage<News> iPage = newsService.queryPage(req.getPageNum(), req.getPageSize(), req.getTitle(), req.getCategoryEnum());
        return R.ok(iPage);
    }

    @PostMapping("/headNews")
    @ApiOperation(value = "头部新闻", notes = "头部新闻")
    public R<HeadNewsResp> headNews() {
        HeadNewsResp headNewsResp = new HeadNewsResp();

        //获取1条最近置顶新闻
        List<News> topList = newsService.findByNewsStatus(NewsStatusEnum.TOP,1);
        if (CollectionUtils.isNotEmpty(topList)) {
            headNewsResp.setTopNews(topList);
        }

        //获取2条最近热门新闻
        List<News> hotList = newsService.findByNewsStatus(NewsStatusEnum.HOT, 2);
        if (CollectionUtils.isNotEmpty(hotList)) {
            headNewsResp.setHotNews(hotList);
        }

        return R.ok(headNewsResp);
    }


}
