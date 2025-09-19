package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.Exposure;
import com.ent.happychat.pojo.req.PageBase;

public interface ExposureService extends IService<Exposure> {

    IPage<Exposure> queryPage(PageBase dto);

    void add(Exposure dto);

    void update(Exposure dto);

}
