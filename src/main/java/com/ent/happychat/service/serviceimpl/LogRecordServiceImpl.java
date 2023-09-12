package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.enums.LogTypeEnum;
import com.ent.happychat.common.tools.HttpTools;
import com.ent.happychat.entity.LogRecord;
import com.ent.happychat.mapper.LogRecordMapper;
import com.ent.happychat.pojo.req.log.LogPageReq;
import com.ent.happychat.service.LogRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogRecordServiceImpl extends ServiceImpl<LogRecordMapper, LogRecord> implements LogRecordService {

    @Override
    public void insert(LogRecord po) {
        po.setCreateTime(LocalDateTime.now());
        po.setRequestUrl(HttpTools.getRequest().getRequestURI());
        save(po);
    }

    @Override
    public List<LogRecord> getList(LogRecord po) {
        IPage<LogRecord> iPage = new Page(1, 5000);
        QueryWrapper<LogRecord> queryWrapper = new QueryWrapper<>();
        if (po.getAccount() != null) {
            queryWrapper.eq("account", po.getAccount());
        }
        if (StringUtils.isNotBlank(po.getIp())) {
            queryWrapper.eq("ip", po.getIp());
        }
        if (StringUtils.isNotBlank(po.getMessage())) {
            queryWrapper.likeRight("message", po.getMessage());
        }
        if (po.getType() != null) {
            queryWrapper.eq("type", po.getType());
        }
        if (StringUtils.isNotBlank(po.getRequestUrl())) {
            queryWrapper.eq("request_url", po.getRequestUrl());
        }
        if (po.getPlatform() != null) {
            queryWrapper.eq("platform", po.getPlatform());
        }
        queryWrapper.orderByDesc("create_time");
        iPage = page(iPage, queryWrapper);
        return iPage.getRecords();
    }

    @Override
    public IPage<LogRecord> page(LogPageReq po) {
        IPage<LogRecord> iPage = new Page<>(po.getPageNum(), po.getPageSize());
        QueryWrapper<LogRecord> queryWrapper = new QueryWrapper<>();
        if (po.getAccount() != null) {
            queryWrapper.eq("account", po.getAccount());
        }
        if (StringUtils.isNotBlank(po.getIp())) {
            queryWrapper.eq("ip", po.getIp());
        }
        if (StringUtils.isNotBlank(po.getMessage())) {
            queryWrapper.likeRight("message", po.getMessage());
        }
        if (po.getType() != null) {
            queryWrapper.eq("type", po.getType());
        }
        if (StringUtils.isNotBlank(po.getRequestUrl())) {
            queryWrapper.eq("request_url", po.getRequestUrl());
        }
        if (po.getPlatform() != null) {
            queryWrapper.eq("platform", po.getPlatform());
        }
        queryWrapper.orderByDesc("create_time");
        return page(iPage, queryWrapper);
    }

    @Override
    public List<LogRecord> registerList(String ip) {
        QueryWrapper<LogRecord> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(ip)) {
            queryWrapper.eq("ip", ip);
        }
        queryWrapper.eq("type", LogTypeEnum.REGISTER.getValue());
        return list(queryWrapper);
    }

}
