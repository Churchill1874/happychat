package com.ent.happychat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ent.happychat.entity.Blacklist;
import com.ent.happychat.entity.News;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Mapper
//@Repository
public interface NewsMapper extends BaseMapper<News> {
    /**
     * 批量插入新闻并且避免重复插入
     * @param newsList
     */
    void insertBatchIgnore(@Param("list") List<News> newsList);

    /**
     * 增加评论数量
     * @param id
     */
    @Async
    void increaseCommentsCount(@Param("id") Long id);

    /**
     * 增加浏览数量
     * @param id
     */
    @Async
    void increaseViewsCount(@Param("id") Long id);

    /**
     * 增加点赞数量
     * @param id
     */
    @Async
    void increaseLikesCount(@Param("id") Long id);

}
