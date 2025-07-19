package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.BetOrder;
import com.ent.happychat.pojo.req.betorder.BetOrderAddReq;
import com.ent.happychat.pojo.req.betorder.BetOrderPageReq;

public interface BetOrderService extends IService<BetOrder> {

    IPage<BetOrder> queryPage(BetOrderPageReq dto);

    void bet(BetOrderAddReq dto);

}
