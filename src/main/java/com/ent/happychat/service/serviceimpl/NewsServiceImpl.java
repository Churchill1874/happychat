package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.enums.NewsCategoryEnum;
import com.ent.happychat.common.constant.enums.NewsStatusEnum;
import com.ent.happychat.entity.News;
import com.ent.happychat.mapper.NewsMapper;
import com.ent.happychat.service.NewsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

    @Override
    public void saveList(List<News> newsList) {
        this.saveList(newsList);
    }

    @Override
    public IPage<News> queryPage(Integer number, Integer size, String title, NewsCategoryEnum categoryEnum) {
        IPage<News> iPage = new Page<>(number, size);

        QueryWrapper<News> queryNews = new QueryWrapper<>();
        queryNews.lambda()
                .eq(categoryEnum != null, News::getCategory, categoryEnum)
                .eq(News::getNewsStatus, NewsStatusEnum.NORMAL)
                .like(StringUtils.isNotBlank(title), News::getTitle, title)
                .orderByDesc(News::getCreateTime);

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
