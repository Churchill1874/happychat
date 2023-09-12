package com.ent.happychat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ent.happychat.entity.LogRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface LogRecordMapper extends BaseMapper<LogRecord> {
}
