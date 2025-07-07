package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.entity.view.LotteryDealerView;
import com.ent.happychat.mapper.PoliticsLotteryDealerMapper;
import com.ent.happychat.pojo.req.lottery.dealer.DealerPageReq;
import com.ent.happychat.service.PoliticsLotteryDealerService;
import org.springframework.stereotype.Service;

@Service
public class PoliticsLotteryDealerServiceImpl extends ServiceImpl<PoliticsLotteryDealerMapper, LotteryDealerView> implements PoliticsLotteryDealerService {

    @Override
    public IPage<LotteryDealerView> queryPage(DealerPageReq dto) {
        IPage<LotteryDealerView> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<LotteryDealerView> queryWrapper = new QueryWrapper<>();
        queryWrapper
            .lambda()
            .eq(dto.getStatus() != null, LotteryDealerView::getStatus, dto.getStatus())
            .orderByDesc(LotteryDealerView::getCreateTime);
        return page(iPage, queryWrapper);
    }

}
