package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.entity.LotteryDealer;
import com.ent.happychat.mapper.LotteryDealerMapper;
import com.ent.happychat.mapper.PlayerInfoMapper;
import com.ent.happychat.service.LotteryDealerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class LotteryDealerServiceImpl extends ServiceImpl<LotteryDealerMapper, LotteryDealer> implements LotteryDealerService {
    @Autowired
    private PlayerInfoMapper playerInfoMapper;

    @Override
    public void add(LotteryDealer dto) {
        playerInfoMapper.reduceBalance(dto.getPlayerId(), dto.getPrizePool());

        dto.setCount1(0);
        dto.setCount2(0);
        dto.setCount3(0);
        dto.setRemainingPrizePool(new BigDecimal(0));
        dto.setCreateTime(LocalDateTime.now());
        save(dto);
    }

}
