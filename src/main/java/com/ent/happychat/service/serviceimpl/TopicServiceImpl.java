package com.ent.happychat.service.serviceimpl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.enums.ViewsEnum;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.Society;
import com.ent.happychat.entity.Topic;
import com.ent.happychat.mapper.SocietyMapper;
import com.ent.happychat.mapper.TopicMapper;
import com.ent.happychat.pojo.req.society.SocietyPageReq;
import com.ent.happychat.pojo.req.topic.TopicAddReq;
import com.ent.happychat.pojo.req.topic.TopicPageReq;
import com.ent.happychat.service.SocietyService;
import com.ent.happychat.service.TopicService;
import com.ent.happychat.service.ViewsRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements TopicService {

    @Autowired
    private ViewsRecordService viewsRecordService;

    @Override
    public IPage<Topic> queryPage(TopicPageReq req) {
        IPage<Topic> iPage = new Page<>(req.getPageNum(), req.getPageSize());

        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(req.getId() != null, Topic::getId, req.getId())
            .eq(req.getIsTop() != null, Topic::getIsTop, req.getIsTop())
            .eq(req.getIsHot() != null, Topic::getIsHot, req.getIsHot())
            .orderByDesc(Topic::getCreateTime)
            .orderByDesc(Topic::getIsTop);

        return page(iPage, queryWrapper);
    }

    @Override
    public void add(TopicAddReq dto) {
        Topic topic = BeanUtil.toBean(dto, Topic.class);
        if (StringUtils.isBlank(topic.getContent())){
            throw new DataException("内容不能为空");
        }
        if (topic.getIsHot() == null){
            topic.setIsHot(false);
        }
        if (topic.getIsTop() == null){
            topic.setIsTop(false);
        }
        if (topic.getStatus() == null){
            topic.setStatus(true);
        }
        if (topic.getCommentsCount() == null){
            topic.setCommentsCount(0);
        }
        if (topic.getViewCount() == null){
            topic.setViewCount(0);
        }
        topic.setCreateTime(LocalDateTime.now());
        topic.setCreateName(TokenTools.getAdminName());
        save(topic);
    }

    @Override
    public void increaseCommentsCount(Long id) {
        baseMapper.increaseCommentsCount(id);
    }

    @Async
    @Override
    public void increaseViewsCount(String ip, Long viewsId, String content, Long playerId, String playerName) {
        viewsRecordService.addViewsRecord(ip, viewsId, content, playerId, playerName, ViewsEnum.TOPIC);
        baseMapper.increaseViewsCount(viewsId);
    }

    @Override
    public Topic findByIdAndInsertRecord(String ip, Long viewsId, Long playerId, String playerName) {
        Topic topic = getById(viewsId);
        increaseViewsCount(ip, viewsId, topic.getTitle(), playerId, playerName);
        return topic;
    }

}
