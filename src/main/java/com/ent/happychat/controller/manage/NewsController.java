package com.ent.happychat.controller.manage;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.entity.News;
import com.ent.happychat.pojo.req.news.NewsPage;
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

@Slf4j
@RestController
@Api(tags = "新闻管理")
@RequestMapping("/manage/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页", notes = "分页")
    public R<IPage<News>> queryPage(@RequestBody @Valid NewsPage req) {
        IPage<News> iPage = newsService.queryPage(req);
        return R.ok(iPage);
    }




}
