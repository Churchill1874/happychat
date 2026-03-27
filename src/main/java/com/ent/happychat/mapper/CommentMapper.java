package com.ent.happychat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ent.happychat.entity.Comment;
import com.ent.happychat.pojo.resp.report.RankInfoResp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    @Select(
            "SELECT player_id AS playerId, COUNT(*) AS commentCount " +
                    "FROM comment " +
                    "GROUP BY player_id " +
                    "ORDER BY commentCount DESC " +
                    "LIMIT 10"
    )
    List<RankInfoResp> top10PlayerRank();
}
