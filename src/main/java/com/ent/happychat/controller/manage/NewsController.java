package com.ent.happychat.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.annotation.AdminLoginCheck;
import com.ent.happychat.common.constant.CacheKeyConstant;
import com.ent.happychat.common.constant.enums.NewsStatusEnum;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.News;
import com.ent.happychat.pojo.req.IdBase;
import com.ent.happychat.pojo.req.news.NewsAddReq;
import com.ent.happychat.pojo.req.news.NewsPageReq;
import com.ent.happychat.pojo.req.news.NewsPullReq;
import com.ent.happychat.pojo.req.news.NewsUpdateBase;
import com.ent.happychat.service.EhcacheService;
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
import java.time.LocalDateTime;

@Slf4j
@RestController
@Api(tags = "新闻管理")
@RequestMapping("/manage/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private EhcacheService ehcacheService;

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页", notes = "分页")
    public R<IPage<News>> queryPage(@RequestBody @Valid NewsPageReq req) {
        IPage<News> iPage = newsService.queryPage(req);
        return R.ok(iPage);
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除某个新闻", notes = "删除某个新闻")
    @AdminLoginCheck
    public R delete(@RequestBody @Valid IdBase req) {
        newsService.removeById(req.getId());

        //清理首页的新闻列表缓存
        ehcacheService.homeNewsCache().remove(CacheKeyConstant.HOME_NEWS_KEY);
        return R.ok(null);
    }

    @PostMapping("/updateNews")
    @ApiOperation(value = "修改编辑新闻", notes = "修改编辑新闻")
    @AdminLoginCheck
    public R updateNews(@RequestBody @Valid NewsUpdateBase req) {
        News news = BeanUtil.toBean(req, News.class);
        newsService.updateNews(news);

        //清理首页的新闻列表缓存
        ehcacheService.homeNewsCache().remove(CacheKeyConstant.HOME_NEWS_KEY);
        return R.ok(null);
    }

    @PostMapping("/addNews")
    @ApiOperation(value = "添加新闻", notes = "添加编辑新闻")
    @AdminLoginCheck
    public R addNews(@RequestBody @Valid NewsAddReq req) {
        News news = BeanUtil.toBean(req, News.class);
        if (news.getNewsStatus() == null){
            news.setNewsStatus(NewsStatusEnum.NORMAL);
        }
        if (news.getSource() == null){
            news.setSource("平台");
        }

        news.setCreateName(TokenTools.getAdminToken(true).getName());
        news.setCreateTime(LocalDateTime.now());
        newsService.save(news);

        //清理首页的新闻列表缓存
        ehcacheService.homeNewsCache().remove(CacheKeyConstant.HOME_NEWS_KEY);
        return R.ok(null);
    }

    @AdminLoginCheck
    @PostMapping("/pullJuHeNews")
    @ApiOperation(value = "拉取聚合新闻", notes = "拉取聚合新闻")
    public R pullNews(@RequestBody @Valid NewsPullReq req) {
        newsService.pullNews(LocalDateTime.now(), req.getNewsCategoryEnum(), false);
        return R.ok(null);
    }



}
