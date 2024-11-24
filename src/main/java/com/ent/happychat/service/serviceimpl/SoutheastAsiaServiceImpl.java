package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.exception.TokenException;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.SoutheastAsia;
import com.ent.happychat.mapper.SoutheastAsiaMapper;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.pojo.req.southeastasia.SoutheastAsiaPageReq;
import com.ent.happychat.service.SoutheastAsiaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SoutheastAsiaServiceImpl extends ServiceImpl<SoutheastAsiaMapper, SoutheastAsia> implements SoutheastAsiaService {
    @Override
    public IPage<SoutheastAsia> queryPage(SoutheastAsiaPageReq req) {
        IPage<SoutheastAsia> iPage = new Page<>(req.getPageNum(), req.getPageSize());

        QueryWrapper<SoutheastAsia> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(req.getId() != null, SoutheastAsia::getId, req.getId())
            .eq(req.getIsTop() != null, SoutheastAsia::getIsTop, req.getIsTop())
            .eq(StringUtils.isNotBlank(req.getSource()), SoutheastAsia::getSource, req.getSource())
            .eq(req.getIsHot() != null, SoutheastAsia::getIsHot, req.getIsHot())
            .eq(req.getIsTop() != null, SoutheastAsia::getIsTop, req.getIsTop())
            .eq(StringUtils.isNotBlank(req.getArea()), SoutheastAsia::getArea, req.getArea())
            .orderByDesc(SoutheastAsia::getIsTop)
            .orderByDesc(SoutheastAsia::getCreateTime);

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
        if (southeastAsia.getCommentCount() == null){
            southeastAsia.setCommentCount(0);
        }
        if (southeastAsia.getReadCount() == null){
            southeastAsia.setReadCount(0);
        }
        southeastAsia.setCreateTime(LocalDateTime.now());
        southeastAsia.setCreateName(TokenTools.getAdminName());
        save(southeastAsia);
    }

}
