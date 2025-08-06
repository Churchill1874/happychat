package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.entity.LotteryDealer;
import com.ent.happychat.mapper.LotteryDealerMapper;
import com.ent.happychat.mapper.PlayerInfoMapper;
import com.ent.happychat.pojo.req.PageBase;
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

        dto.setBet1Amount(BigDecimal.ZERO);
        dto.setBet2Amount(BigDecimal.ZERO);
        dto.setBet3Amount(BigDecimal.ZERO);
        dto.setRemainingPrizePool(dto.getPrizePool());
        dto.setCreateTime(LocalDateTime.now());
        save(dto);
    }

    @Override
    public IPage<LotteryDealer> queryPage(PageBase dto) {
        IPage<LotteryDealer> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());

        QueryWrapper<LotteryDealer> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(LotteryDealer::getCreateTime);
        return page(iPage, queryWrapper);
    }

}
