package com.ent.happychat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ent.happychat.entity.InteractiveStatistics;
import org.apache.ibatis.annotations.Param;

public interface InteractiveStatisticsMapper extends BaseMapper<InteractiveStatistics> {

    void addCollect(@Param("playerId") Long playerId);

    void addLikesReceived(@Param("playerId") Long playerId);

    void addFollowers(@Param("playerId") Long playerId);


}
