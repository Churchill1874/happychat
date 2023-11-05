package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.common.constant.enums.NewsCategoryEnum;
import com.ent.happychat.entity.News;

import java.util.List;

public interface NewsService extends IService<News> {

    /**
     * 批量保存新闻
     * @param newsList
     */
    void saveList(List<News> newsList);

    /**
     * 分页查询新闻
     * @param number
     * @param size
     * @param title
     * @param categoryEnum
     */
    IPage<News> queryPage(Integer number, Integer size, String title, NewsCategoryEnum categoryEnum);


}
