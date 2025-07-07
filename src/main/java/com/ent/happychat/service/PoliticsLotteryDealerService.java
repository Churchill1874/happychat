package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.view.LotteryDealerView;
import com.ent.happychat.pojo.req.lottery.dealer.DealerPageReq;

public interface PoliticsLotteryDealerService extends IService<LotteryDealerView> {

    IPage<LotteryDealerView> queryPage(DealerPageReq dto);

}
