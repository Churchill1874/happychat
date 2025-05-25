package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.enums.ViewsEnum;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.exception.TokenException;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.Politics;
import com.ent.happychat.entity.SoutheastAsia;
import com.ent.happychat.mapper.SoutheastAsiaMapper;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.pojo.req.southeastasia.SoutheastAsiaPageReq;
import com.ent.happychat.service.SoutheastAsiaService;
import com.ent.happychat.service.ViewsRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SoutheastAsiaServiceImpl extends ServiceImpl<SoutheastAsiaMapper, SoutheastAsia> implements SoutheastAsiaService {

    @Autowired
    private ViewsRecordService viewsRecordService;

    @Override
    public IPage<SoutheastAsia> queryPage(SoutheastAsiaPageReq req) {
        IPage<SoutheastAsia> iPage = new Page<>(req.getPageNum(), req.getPageSize());

        QueryWrapper<SoutheastAsia> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(req.getId() != null, SoutheastAsia::getId, req.getId())
            .eq(req.getIsTop() != null, SoutheastAsia::getIsTop, req.getIsTop())
            .eq(StringUtils.isNotBlank(req.getSource()), SoutheastAsia::getSource, req.getSource())
            .eq(req.getIsHot() != null, SoutheastAsia::getIsHot, req.getIsHot())
            .eq(StringUtils.isNotBlank(req.getArea()), SoutheastAsia::getArea, req.getArea())
            .orderByDesc(SoutheastAsia::getCreateTime)
            .orderByDesc(SoutheastAsia::getIsTop);

        return page(iPage, queryWrapper);
    }

    @Override
    public void add(SoutheastAsia southeastAsia) {
        if (StringUtils.isBlank(southeastAsia.getSource())){
            throw new DataException("新闻来源不能为空");
        }
        if (StringUtils.isBlank(southeastAsia.getArea())){
            throw new DataException("新闻所属区域不能为空");
        }
        if (StringUtils.isBlank(southeastAsia.getContent())){
            throw new DataException("内容不能为空");
        }
        if (southeastAsia.getIsHot() == null){
            southeastAsia.setIsHot(false);
        }
        if (southeastAsia.getIsTop() == null){
            southeastAsia.setIsTop(false);
        }
        if (southeastAsia.getStatus() == null){
            southeastAsia.setStatus(true);
        }
        if (southeastAsia.getCommentsCount() == null){
            southeastAsia.setCommentsCount(0);
        }
        if (southeastAsia.getViewCount() == null){
            southeastAsia.setViewCount(0);
        }
        southeastAsia.setCreateTime(LocalDateTime.now());
        southeastAsia.setCreateName(TokenTools.getAdminName());
        save(southeastAsia);
    }

    @Override
    public void increaseCommentsCount(Long id) {
        baseMapper.increaseCommentsCount(id);
    }

    @Async
    @Override
    public void increaseViewsCount(String ip, Long viewsId, String content, Long playerId, String playerName) {
        viewsRecordService.addViewsRecord(ip, viewsId, content, playerId, playerName, ViewsEnum.SOUTHEAST_ASIA);
        baseMapper.increaseViewsCount(viewsId);
    }

    @Override
    public SoutheastAsia findByIdAndInsertRecord(String ip, Long viewsId, Long playerId, String playerName) {
        SoutheastAsia southeastAsia = getById(viewsId);
        increaseViewsCount(ip, viewsId, southeastAsia.getTitle(), playerId, playerName);
        return southeastAsia;
    }

}
