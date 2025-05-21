package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.enums.ViewsEnum;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.Society;
import com.ent.happychat.entity.SoutheastAsia;
import com.ent.happychat.mapper.SocietyMapper;
import com.ent.happychat.mapper.SoutheastAsiaMapper;
import com.ent.happychat.pojo.req.society.SocietyPageReq;
import com.ent.happychat.pojo.req.southeastasia.SoutheastAsiaPageReq;
import com.ent.happychat.service.SocietyService;
import com.ent.happychat.service.SoutheastAsiaService;
import com.ent.happychat.service.ViewsRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SocietyServiceImpl extends ServiceImpl<SocietyMapper, Society> implements SocietyService {

    @Autowired
    private ViewsRecordService viewsRecordService;

    @Override
    public IPage<Society> queryPage(SocietyPageReq req) {
        IPage<Society> iPage = new Page<>(req.getPageNum(), req.getPageSize());

        QueryWrapper<Society> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(req.getId() != null, Society::getId, req.getId())
            .eq(req.getIsTop() != null, Society::getIsTop, req.getIsTop())
            .eq(StringUtils.isNotBlank(req.getSource()), Society::getSource, req.getSource())
            .eq(req.getIsHot() != null, Society::getIsHot, req.getIsHot())
            .eq(req.getIsTop() != null, Society::getIsTop, req.getIsTop())
            .eq(StringUtils.isNotBlank(req.getArea()), Society::getArea, req.getArea())
            .orderByDesc(Society::getCreateTime)
            .orderByDesc(Society::getIsTop);

        return page(iPage, queryWrapper);
    }

    @Override
    public void add(Society society) {
        if (StringUtils.isBlank(society.getSource())){
            throw new DataException("新闻来源不能为空");
        }
        if (StringUtils.isBlank(society.getArea())){
            throw new DataException("新闻所属区域不能为空");
        }
        if (StringUtils.isBlank(society.getContent())){
            throw new DataException("内容不能为空");
        }
        if (society.getIsHot() == null){
            society.setIsHot(false);
        }
        if (society.getIsTop() == null){
            society.setIsTop(false);
        }
        if (society.getStatus() == null){
            society.setStatus(true);
        }
        if (society.getCommentsCount() == null){
            society.setCommentsCount(0);
        }
        if (society.getViewCount() == null){
            society.setViewCount(0);
        }
        society.setCreateTime(LocalDateTime.now());
        society.setCreateName(TokenTools.getAdminName());
        save(society);
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
    public Society findByIdAndInsertRecord(String ip, Long viewsId, Long playerId, String playerName) {
        Society society = getById(viewsId);
        increaseViewsCount(ip, viewsId, society.getTitle(), playerId, playerName);
        return society;
    }

}
