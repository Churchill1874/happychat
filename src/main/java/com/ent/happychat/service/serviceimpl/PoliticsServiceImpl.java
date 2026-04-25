package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.enums.InfoEnum;
import com.ent.happychat.common.constant.enums.LikesEnum;
import com.ent.happychat.common.constant.enums.ViewsEnum;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.entity.LikesRecord;
import com.ent.happychat.entity.Politics;
import com.ent.happychat.mapper.PoliticsMapper;
import com.ent.happychat.pojo.req.politics.PoliticsPageReq;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.service.LikesRecordService;
import com.ent.happychat.service.PoliticsService;
import com.ent.happychat.service.ViewsRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Service
public class PoliticsServiceImpl extends ServiceImpl<PoliticsMapper, Politics> implements PoliticsService {

    @Autowired
    private LikesRecordService likesRecordService;
    @Autowired
    private ViewsRecordService viewsRecordService;

    @Override
    public IPage<Politics> queryPage(PoliticsPageReq dto) {
        IPage<Politics> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<Politics> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(dto.getId() != null, Politics::getId, dto.getId())
                .like(StringUtils.isNotBlank(dto.getTitle()), Politics::getTitle, dto.getTitle())
                .eq(dto.getNewsStatus() != null , Politics::getNewsStatus, dto.getNewsStatus())
                .eq(StringUtils.isNotBlank(dto.getSource()), Politics::getSource, dto.getSource())
                .eq(StringUtils.isNotBlank(dto.getCountry()), Politics::getCountry, dto.getCountry())
                .orderByDesc(Politics::getCreateTime);
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
            .eq(LikesRecord::getInfoType, InfoEnum.POLITICS)
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

    @Override
    public Map<Long, Politics> mapByIds(List<Long> ids) {
        Map<Long, Politics> map = new HashMap<>();
        if(CollectionUtils.isEmpty(ids)){
            return map;
        }

        QueryWrapper<Politics> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(Politics::getId, Politics::getTitle).in(Politics::getId, ids);
        List<Politics> list = list(queryWrapper);

        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(news -> {
                map.put(news.getId(), news);
            });
        }
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pullNews() {
/*        List<Politics> politicsList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(newsDTO -> {
                Politics politics = new Politics();
                politics.setTitle(newsDTO.getTitleZh());
                politics.setContent(newsDTO.getSummaryZh());
                politics.setViewCount(0);
                politics.setLikesCount(0);
                politics.setCommentsCount(0);
                politics.setNewsStatus(NewsStatusEnum.NORMAL);
                politics.setImagePath(newsDTO.getImageUrl());
                politics.setCountry("国际");
                politics.setSource(newsDTO.getSourceName());
                politics.setCreateTime(LocalDateTime.now());
                politics.setCreateName("GNews");
                politicsList.add(politics);
            });
        }
        log.info("GNews拉取新闻成功,准备入库数量：{}", politicsList.size());
        saveBatch(politicsList);*/
    }


}
