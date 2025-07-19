package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.entity.PoliticsLottery;
import com.ent.happychat.mapper.PoliticsLotteryMapper;
import com.ent.happychat.pojo.req.lottery.dealer.DealerPageReq;
import com.ent.happychat.service.PoliticsLotteryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PoliticsLotteryServiceImpl extends ServiceImpl<PoliticsLotteryMapper, PoliticsLottery> implements PoliticsLotteryService {

    @Override
    public IPage<PoliticsLottery> queryPage(DealerPageReq dto) {
        IPage<PoliticsLottery> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<PoliticsLottery> queryWrapper = new QueryWrapper<>();
        queryWrapper
            .lambda()
            .eq(dto.getStatus() != null, PoliticsLottery::getStatus, dto.getStatus())
            .eq(PoliticsLottery::getIsOnSale, true)
            .gt(PoliticsLottery::getEndTime, LocalDateTime.now())
            .orderByDesc(PoliticsLottery::getCreateTime);
        return page(iPage, queryWrapper);
    }

}
