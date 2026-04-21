package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.entity.LogInfo;
import com.ent.happychat.mapper.LogInfoMapper;
import com.ent.happychat.pojo.req.login.LogInfoPage;
import com.ent.happychat.service.LogInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class LogInfoServiceImpl extends ServiceImpl<LogInfoMapper, LogInfo> implements LogInfoService {

    @Async
    @Override
    public void asyncSave(LogInfo logInfo) {
        save(logInfo);
    }

    @Override
    public IPage<LogInfo> queryPage(LogInfoPage req) {
        IPage<LogInfo> iPage = new Page<>(req.getPageNum(), req.getPageSize());

        LambdaQueryWrapper<LogInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(req.getType() != null, LogInfo::getType, req.getType())
                .eq(StringUtils.isNotBlank(req.getIp()), LogInfo::getIp, req.getIp())
                .eq(req.getPlayerId() != null, LogInfo::getPlayerId, req.getPlayerId())
                .like(StringUtils.isNotBlank(req.getContent()), LogInfo::getContent, req.getContent())
                .eq(StringUtils.isNotBlank(req.getCreateName()), LogInfo::getCreateName, req.getCreateName())
                .orderByDesc(LogInfo::getCreateTime);
        return page(iPage, queryWrapper);
    }


}
