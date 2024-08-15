package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.entity.Job;
import com.ent.happychat.mapper.JobMapper;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {
    @Override
    public IPage<Job> queryPage(PageBase po) {
        IPage<Job> iPage = new Page<>(po.getPageNum(), po.getPageSize());
        QueryWrapper<Job> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(Job::getCreateTime);
        return page(iPage, queryWrapper);
    }

}
