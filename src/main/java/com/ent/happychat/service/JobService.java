package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.Job;
import com.ent.happychat.pojo.req.PageBase;

public interface JobService extends IService<Job> {

    IPage<Job> queryPage(PageBase po);


}
