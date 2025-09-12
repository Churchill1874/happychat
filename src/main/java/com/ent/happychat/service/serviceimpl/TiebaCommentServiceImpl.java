package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.entity.Tieba;
import com.ent.happychat.entity.TiebaComment;
import com.ent.happychat.mapper.TiebaCommentMapper;
import com.ent.happychat.mapper.TiebaMapper;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.service.TiebaCommentService;
import com.ent.happychat.service.TiebaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class TiebaCommentServiceImpl extends ServiceImpl<TiebaCommentMapper, TiebaComment> implements TiebaCommentService {

    @Autowired
    private TiebaMapper tiebaMapper;
    @Autowired
    private TiebaService tiebaService;

    @Override
    public IPage<TiebaComment> queryPage(PageBase pageBase, Long playerId, Long tiebaId) {
        IPage<TiebaComment> iPage = new Page<>(pageBase.getPageNum(), pageBase.getPageSize());
        QueryWrapper<TiebaComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(playerId != null, TiebaComment::getPlayerId, playerId)
            .eq(tiebaId != null, TiebaComment::getTiebaId, tiebaId)
            .orderByDesc(TiebaComment::getCreateTime);
        return page(iPage, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void send(TiebaComment tiebaComment) {
        tiebaMapper.increaseCommentsCount(tiebaComment.getTiebaId());

        tiebaService.updateLastCommentTime(tiebaComment.getTiebaId());

        tiebaComment.setCreateTime(LocalDateTime.now());
        save(tiebaComment);
    }

}
