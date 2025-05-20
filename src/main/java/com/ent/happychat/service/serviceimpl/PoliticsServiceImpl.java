package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.enums.InfoEnum;
import com.ent.happychat.common.constant.enums.LikesEnum;
import com.ent.happychat.common.constant.enums.ViewsEnum;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.exception.TokenException;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.LikesRecord;
import com.ent.happychat.entity.News;
import com.ent.happychat.entity.Politics;
import com.ent.happychat.mapper.PoliticsMapper;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.service.LikesRecordService;
import com.ent.happychat.service.PoliticsService;
import com.ent.happychat.service.ViewsRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class PoliticsServiceImpl extends ServiceImpl<PoliticsMapper, Politics> implements PoliticsService {

    @Autowired
    private LikesRecordService likesRecordService;
    @Autowired
    private ViewsRecordService viewsRecordService;

    @Override
    public IPage<Politics> queryPage(PageBase dto) {
        IPage<Politics> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<Politics> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(Politics::getCreateTime);
        return page(iPage, queryWrapper);
    }

    @Override
    public void increaseLikesCount(Long id, PlayerTokenResp playerTokenResp) {
        Politics politics = getById(id);
        if (politics == null){
            throw new DataException("内容不存在或已删除");
        }
        QueryWrapper<LikesRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(LikesRecord::getPlayerId, playerTokenResp.getId())
            .eq(LikesRecord::getLikesId, id)
            .eq(LikesRecord::getLikesType, LikesEnum.NEWS);
        int count = likesRecordService.count(queryWrapper);

        if(count == 0){
            likesRecordService.increaseLikesCount(
                playerTokenResp.getId(),
                playerTokenResp.getName(),
                id,
                politics.getTitle(),
                LikesEnum.NEWS,
                null,
                InfoEnum.POLITICS
            );
            baseMapper.increaseLikesCount(id);
        }
    }

    @Async
    @Override
    public void increaseViewsCount(String ip, Long viewsId, String content, Long playerId, String playerName) {
        viewsRecordService.addViewsRecord(ip, viewsId, content, playerId, playerName, ViewsEnum.POLITICS);
        baseMapper.increaseViewsCount(viewsId);
    }

    @Async
    @Override
    public void increaseCommentsCount(Long id) {
        baseMapper.increaseCommentsCount(id);
    }

    @Override
    public Politics findByIdAndInsertRecord(String ip, Long viewsId, Long playerId, String playerName) {
        Politics politics = getById(viewsId);
        increaseViewsCount(ip, viewsId, politics.getTitle(), playerId, playerName);
        return politics;
    }

}
