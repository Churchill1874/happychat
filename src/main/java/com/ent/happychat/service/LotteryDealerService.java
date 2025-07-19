package com.ent.happychat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.LotteryDealer;

public interface LotteryDealerService extends IService<LotteryDealer> {

    void add(LotteryDealer dto);

}
