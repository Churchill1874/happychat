package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.entity.CompanyEvent;
import com.ent.happychat.mapper.CompanyEventMapper;
import com.ent.happychat.service.CompanyEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CompanyEventServiceImpl extends ServiceImpl<CompanyEventMapper, CompanyEvent> implements CompanyEventService {

    @Override
    public Map<Long, List<CompanyEvent>> mapGroup(List<Long> companyIdList) {
        Map<Long, List<CompanyEvent>> map = new HashMap<>();

        QueryWrapper<CompanyEvent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .in(CompanyEvent::getCompanyId, companyIdList)
                .orderByDesc(CompanyEvent::getEventDate);
        List<CompanyEvent> companyEventList = list(queryWrapper);

        if (CollectionUtils.isEmpty(companyEventList)){
            return map;
        }

        return companyEventList.stream().collect(Collectors.groupingBy(CompanyEvent::getCompanyId));
    }

    @Override
    public List<CompanyEvent> findByCompanyId(Long companyId) {
        if(companyId == null){
            return null;
        }
        LambdaQueryWrapper<CompanyEvent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(CompanyEvent::getCompanyId, companyId)
                .orderByDesc(CompanyEvent::getCreateTime);
        return list(queryWrapper);
    }


}
