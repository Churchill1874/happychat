package com.ent.happychat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ent.happychat.entity.TiebaComment;
import org.apache.ibatis.annotations.Param;

public interface TiebaCommentMapper extends BaseMapper<TiebaComment> {


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



}
