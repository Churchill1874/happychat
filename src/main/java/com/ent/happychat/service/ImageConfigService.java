package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.ImageConfig;
import com.ent.happychat.pojo.req.image.ImageConfigPageReq;

public interface ImageConfigService extends IService<ImageConfig> {

    IPage<ImageConfig> queryPage(ImageConfigPageReq po);

}
