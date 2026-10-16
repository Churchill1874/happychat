package com.ent.happychat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ent.happychat.entity.Vote;
import org.apache.ibatis.annotations.Param;

public interface VoteMapper extends BaseMapper<Vote> {

    /**
     * 增加评论数量
     */
    void increaseCommentsCount(@Param("id") Long id);

    /**
     * 增加浏览数量
     */
    void increaseViewsCount(@Param("id") Long id);

    /**
     * 增减参与投票人数
     */
    void changeVoterCount(@Param("id") Long id, @Param("delta") int delta);

    /**
     * 按投票记录重新统计参与人数
     */
    void recountVoterCount(@Param("id") Long id);

}
