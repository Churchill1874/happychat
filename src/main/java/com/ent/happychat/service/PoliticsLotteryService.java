package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.PoliticsLottery;
import com.ent.happychat.pojo.req.lottery.dealer.DealerPageReq;

public interface PoliticsLotteryService extends IService<PoliticsLottery> {
    IPage<PoliticsLottery> queryPage(DealerPageReq politicsLottery);


}
