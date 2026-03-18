package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.enums.ViewsEnum;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.Exposure;
import com.ent.happychat.entity.SoutheastAsia;
import com.ent.happychat.mapper.ExposureMapper;
import com.ent.happychat.pojo.req.exposure.ExposurePage;
import com.ent.happychat.service.ExposureService;
import com.ent.happychat.service.ViewsRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class ExposureServiceImpl extends ServiceImpl<ExposureMapper, Exposure> implements ExposureService {

    @Resource
    private ViewsRecordService viewsRecordService;

    @Override
    public IPage<Exposure> queryPage(ExposurePage dto) {
        IPage<Exposure> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<Exposure> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(dto.getIsTop() != null, Exposure::getIsTop, dto.getIsTop())
                .eq(dto.getLevel() != null, Exposure::getLevel, dto.getLevel())
                .like(StringUtils.isNotBlank(dto.getTitle()), Exposure::getTitle, dto.getTitle())
                .like(StringUtils.isNotBlank(dto.getAddress()), Exposure::getAddress, dto.getAddress())
                .orderByDesc(Exposure::getIsTop)
                .orderByDesc(Exposure::getCreateTime);
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

    @Async
    @Override
    public void increaseViewsCount(String ip, Long viewsId, String content, Long playerId, String playerName) {
        viewsRecordService.addViewsRecord(ip, viewsId, content, playerId, playerName, ViewsEnum.EXPOSURE);
        baseMapper.increaseViewsCount(viewsId);
    }

    @Override
    public Exposure findByIdAndInsertRecord(String ip, Long viewsId, Long playerId, String playerName) {
        Exposure exposure = getById(viewsId);
        increaseViewsCount(ip, viewsId, exposure.getTitle(), playerId, playerName);
        return exposure;
    }

}
