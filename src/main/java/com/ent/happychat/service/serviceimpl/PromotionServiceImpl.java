package com.ent.happychat.service.serviceimpl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.enums.ViewsEnum;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.Promotion;
import com.ent.happychat.entity.Society;
import com.ent.happychat.mapper.PromotionMapper;
import com.ent.happychat.mapper.SocietyMapper;
import com.ent.happychat.pojo.req.promotion.PromotionAddReq;
import com.ent.happychat.pojo.req.promotion.PromotionPageReq;
import com.ent.happychat.pojo.req.society.SocietyPageReq;
import com.ent.happychat.service.PromotionService;
import com.ent.happychat.service.SocietyService;
import com.ent.happychat.service.ViewsRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PromotionServiceImpl extends ServiceImpl<PromotionMapper, Promotion> implements PromotionService {

    @Autowired
    private ViewsRecordService viewsRecordService;

    @Override
    public IPage<Promotion> queryPage(PromotionPageReq req) {
        IPage<Promotion> iPage = new Page<>(req.getPageNum(), req.getPageSize());

        QueryWrapper<Promotion> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(req.getId() != null, Promotion::getId, req.getId())
            .eq(req.getIsTop() != null, Promotion::getIsTop, req.getIsTop())
            .eq(req.getType() != null , Promotion::getType, req.getType())
            .eq(StringUtils.isNotBlank(req.getContact()), Promotion::getContact, req.getContact())
            .eq(StringUtils.isNotBlank(req.getArea()), Promotion::getArea, req.getArea())
            .orderByDesc(Promotion::getCreateTime)
            .orderByDesc(Promotion::getIsTop);

        return page(iPage, queryWrapper);
    }

    @Override
    public void add(PromotionAddReq dto) {
        Promotion promotion = BeanUtil.toBean(dto, Promotion.class);
        if (dto.getIsTop() == null){
            dto.setIsTop(false);
        }
        if (dto.getStatus() == null){
            dto.setStatus(true);
        }
        if (dto.getCommentsCount() == null){
            dto.setCommentsCount(0);
        }
        if (dto.getViewCount() == null){
            dto.setViewCount(0);
        }
        promotion.setCreateTime(LocalDateTime.now());
        promotion.setCreateName(TokenTools.getAdminName());
        save(promotion);
    }

    @Override
    public void increaseCommentsCount(Long id) {
        baseMapper.increaseCommentsCount(id);
    }

    @Async
    @Override
    public void increaseViewsCount(String ip, Long viewsId, String content, Long playerId, String playerName) {
        viewsRecordService.addViewsRecord(ip, viewsId, content, playerId, playerName, ViewsEnum.SOCIETY);
        baseMapper.increaseViewsCount(viewsId);
    }

    @Override
    public Promotion findByIdAndInsertRecord(String ip, Long viewsId, Long playerId, String playerName) {
        Promotion promotion = getById(viewsId);
        increaseViewsCount(ip, viewsId, promotion.getTitle(), playerId, playerName);
        return promotion;
    }

}
