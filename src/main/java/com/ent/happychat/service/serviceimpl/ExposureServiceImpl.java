package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.Exposure;
import com.ent.happychat.mapper.ExposureMapper;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.service.ExposureService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ExposureServiceImpl extends ServiceImpl<ExposureMapper, Exposure> implements ExposureService {

    @Override
    public IPage<Exposure> queryPage(PageBase dto) {
        IPage<Exposure> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<Exposure> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(Exposure::getIsTop,Exposure::getCreateTime);
        return page(iPage, queryWrapper);
    }

    @Override
    public void add(Exposure dto) {
        dto.setCreateTime(LocalDateTime.now());
        dto.setCreateName(TokenTools.getAdminName());
        save(dto);
    }

    @Override
    public void update(Exposure dto) {
        updateById(dto);
    }


}
