package com.ent.happychat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ent.happychat.entity.Blacklist;
import com.ent.happychat.entity.News;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Mapper
//@Repository
public interface NewsMapper extends BaseMapper<News> {

    //批量插入新闻并且避免重复插入
    void insertBatchIgnore(@Param("list") List<News> newsList);

}
