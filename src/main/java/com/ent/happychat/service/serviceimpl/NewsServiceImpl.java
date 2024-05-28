package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.enums.NewsCategoryEnum;
import com.ent.happychat.common.constant.enums.NewsStatusEnum;
import com.ent.happychat.entity.News;
import com.ent.happychat.mapper.NewsMapper;
import com.ent.happychat.pojo.req.news.NewsPage;
import com.ent.happychat.service.NewsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

    @Override
    public void saveList(List<News> newsList) {
        this.saveBatch(newsList);
    }

    @Override
    public IPage<News> queryPage(NewsPage newsPage) {
        IPage<News> iPage = new Page<>(newsPage.getPageNum(), newsPage.getPageSize());

        QueryWrapper<News> queryNews = new QueryWrapper<>();
        queryNews.lambda()
                .eq(newsPage.getCategoryEnum() != null, News::getCategory, newsPage.getCategoryEnum())
                .eq(newsPage.getNewsStatus() != null, News::getNewsStatus, newsPage.getNewsStatus())
                .like(StringUtils.isNotBlank(newsPage.getTitle()), News::getTitle, newsPage.getTitle())
                .orderByDesc(News::getCreateTime);

        //如果没有要求按照 差评 点赞 浏览 评论数量 要求筛选
        boolean createTimeSort = !newsPage.getBadSort() && !newsPage.getLikesSort() && !newsPage.getViewSort() && !newsPage.getCommentsSort();
        if (createTimeSort){
            queryNews.lambda().orderByDesc(News::getCreateTime);
        }
        if (!createTimeSort){
            if (newsPage.getBadSort()){
                queryNews.lambda().orderByDesc(News::getBadCount);
            }
            if (newsPage.getLikesSort()){
                queryNews.lambda().orderByDesc(News::getLikesCount);
            }
            if (newsPage.getViewSort()){
                queryNews.lambda().orderByDesc(News::getViewCount);
            }
            if (newsPage.getCommentsSort()){
                queryNews.lambda().orderByDesc(News::getCommentsCount);
            }
        }

        return page(iPage, queryNews);
    }

    @Override
    public List<News> findByNewsStatus(NewsStatusEnum newsStatusEnum ,Integer size) {
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(News::getNewsStatus, newsStatusEnum)
                .orderByDesc(News::getCreateTime)
                .last("LIMIT " + size);

        return this.list(queryWrapper);
    }

}
