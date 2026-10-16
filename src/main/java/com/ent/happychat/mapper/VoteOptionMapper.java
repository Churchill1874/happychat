package com.ent.happychat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ent.happychat.entity.VoteOption;
import org.apache.ibatis.annotations.Param;

public interface VoteOptionMapper extends BaseMapper<VoteOption> {

    /**
     * 增减选项得票数，不会小于0
     */
    void changeVoteCount(@Param("id") Long id, @Param("delta") int delta);

}
