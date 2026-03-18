package com.ent.happychat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ent.happychat.entity.Exposure;
import org.apache.ibatis.annotations.Param;

public interface ExposureMapper extends BaseMapper<Exposure> {

    /**
     * 增加浏览数量
     * @param id
     */
    void increaseViewsCount(@Param("id") Long id);
}
