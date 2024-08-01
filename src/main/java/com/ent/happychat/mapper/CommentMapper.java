package com.ent.happychat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ent.happychat.entity.Comment;
import org.apache.ibatis.annotations.Param;

public interface CommentMapper extends BaseMapper<Comment> {


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
