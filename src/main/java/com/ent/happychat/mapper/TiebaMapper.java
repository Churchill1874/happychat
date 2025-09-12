package com.ent.happychat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ent.happychat.entity.Tieba;
import org.apache.ibatis.annotations.Param;


public interface TiebaMapper extends BaseMapper<Tieba> {


    /**
     * 增加评论数量
     *
     * @param id
     */
    void increaseCommentsCount(@Param("id") Long id);

    /**
     * 增加点赞数量
     *
     * @param id
     */
    void increaseLikesCount(@Param("id") Long id);

    /**
     * 增加评论数量
     * @param id
     */
    void increaseViewCount(@Param("id") Long id);


}
