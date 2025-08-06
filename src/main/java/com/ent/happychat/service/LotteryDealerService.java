package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.LotteryDealer;
import com.ent.happychat.pojo.req.PageBase;

public interface LotteryDealerService extends IService<LotteryDealer> {

    void add(LotteryDealer dto);

    IPage<LotteryDealer> queryPage(PageBase dto);

}
