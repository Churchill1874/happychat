package com.ent.happychat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ent.happychat.entity.PlayerInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface PlayerInfoMapper extends BaseMapper<PlayerInfo> {

    void reduceBalance(@Param("playerId") Long playerId, @Param("amount") BigDecimal amount);
}
