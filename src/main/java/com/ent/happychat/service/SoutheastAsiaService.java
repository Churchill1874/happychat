package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.SoutheastAsia;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.pojo.req.southeastasia.SoutheastAsiaPageReq;

public interface SoutheastAsiaService extends IService<SoutheastAsia> {

    IPage<SoutheastAsia> queryPage(SoutheastAsiaPageReq req);

    void add(SoutheastAsia req);


}
