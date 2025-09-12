package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.entity.Tieba;
import com.ent.happychat.mapper.TiebaMapper;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.service.TiebaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class TiebaServiceImpl extends ServiceImpl<TiebaMapper, Tieba> implements TiebaService {

    @Override
    public Tieba findById(Long id) {
        baseMapper.increaseViewCount(id);
        return getById(id);
    }

    @Override
    public IPage<Tieba> queryPage(PageBase pageBase, Long playerId, String title) {
        IPage<Tieba> iPage = new Page<>(pageBase.getPageNum(), pageBase.getPageSize());
        QueryWrapper<Tieba> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(playerId != null, Tieba::getPlayerId, playerId)
            .eq(StringUtils.isNotBlank(title), Tieba::getTitle, title)
            .orderByDesc(Tieba::getCreateTime);
        return page(iPage, queryWrapper);
    }

    @Override
    public void send(Tieba tieba) {
        tieba.setCreateTime(LocalDateTime.now());
        tieba.setIsTop(false);
        tieba.setIsHot(false);
        save(tieba);
    }

    @Override
    public void updateLastCommentTime(Long id) {
        UpdateWrapper<Tieba> updateWrapper = new UpdateWrapper<>();
        updateWrapper
            .lambda()
            .set(Tieba::getCreateTime, LocalDateTime.now())
            .eq(Tieba::getId,id);
        update(updateWrapper);
    }


}
