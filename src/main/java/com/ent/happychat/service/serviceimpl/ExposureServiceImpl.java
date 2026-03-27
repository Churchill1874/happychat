package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.enums.ViewsEnum;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.Exposure;
import com.ent.happychat.entity.News;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        LambdaUpdateWrapper<Exposure> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(Exposure::getId, dto.getId())
                .set(Exposure::getImage1, dto.getImage1())
                .set(Exposure::getImage2, dto.getImage2())
                .set(Exposure::getImage3, dto.getImage3())
                .set(Exposure::getImage4, dto.getImage4())
                .set(Exposure::getImage5, dto.getImage5())
                .set(Exposure::getImage6, dto.getImage6());
        update(updateWrapper);
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

    @Override
    public Map<Long, Exposure> mapByIds(List<Long> ids) {
        Map<Long, Exposure> map = new HashMap<>();
        if(CollectionUtils.isEmpty(ids)){
            return map;
        }

        QueryWrapper<Exposure> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(Exposure::getId, Exposure::getTitle).in(Exposure::getId, ids);
        List<Exposure> list = list(queryWrapper);

        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(news -> {
                map.put(news.getId(), news);
            });
        }
        return map;
    }

}
