package com.ent.happychat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ent.happychat.entity.FootballMatch;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface FootballMatchMapper extends BaseMapper<FootballMatch> {

}
