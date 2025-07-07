package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.entity.LotteryDealer;
import com.ent.happychat.mapper.LotteryDealerMapper;
import com.ent.happychat.service.LotteryDealerService;
import org.springframework.stereotype.Service;

@Service
public class LotteryDealerServiceImpl extends ServiceImpl<LotteryDealerMapper, LotteryDealer> implements LotteryDealerService {
}
