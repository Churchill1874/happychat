package com.ent.happychat.service.serviceimpl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.enums.DrawStatusEnum;
import com.ent.happychat.common.constant.enums.LotteryTypeEnum;
import com.ent.happychat.entity.BetOrder;
import com.ent.happychat.entity.LotteryDealer;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.entity.PoliticsLottery;
import com.ent.happychat.mapper.BetOrderMapper;
import com.ent.happychat.mapper.LotteryDealerMapper;
import com.ent.happychat.pojo.req.betorder.BetOrderAddReq;
import com.ent.happychat.pojo.req.betorder.BetOrderPageReq;
import com.ent.happychat.service.BetOrderService;
import com.ent.happychat.service.LotteryDealerService;
import com.ent.happychat.service.PlayerInfoService;
import com.ent.happychat.service.PoliticsLotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class BetOrderServiceImpl extends ServiceImpl<BetOrderMapper, BetOrder> implements BetOrderService {

    @Autowired
    private PlayerInfoService playerInfoService;
    @Autowired
    private LotteryDealerMapper lotteryDealerMapper;
    @Autowired
    private LotteryDealerService lotteryDealerService;
    @Autowired
    private PoliticsLotteryService politicsLotteryService;

    @Override
    public IPage<BetOrder> queryPage(BetOrderPageReq dto) {
        IPage<BetOrder> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<BetOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(BetOrder::getType, dto.getType())
            .eq(dto.getPlayerId() != null, BetOrder::getPlayerId, dto.getPlayerId())
            .orderByDesc(BetOrder::getCreateTime);
        return page(iPage, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void bet(BetOrderAddReq dto) {
        BetOrder betOrder = BeanUtil.toBean(dto, BetOrder.class);
        betOrder.setCreateTime(LocalDateTime.now());

        //庄家设置信息
        LotteryDealer lotteryDealer = lotteryDealerService.getById(dto.getDealerId());
        //庄家账号信息
        PlayerInfo playerInfo = playerInfoService.getById(lotteryDealer.getPlayerId());
        //中奖金额
        BigDecimal amount = lotteryDealer.getWinnerAmount(dto.getChooseNumber(), dto.getBetAmount());
        //更新统计投注数量和支持率
        lotteryDealer.addCount(dto.getChooseNumber());
        //减少奖池
        lotteryDealer.setRemainingPrizePool(lotteryDealer.getRemainingPrizePool().subtract(amount));
        //更新盘口信息
        lotteryDealerService.updateById(lotteryDealer);

        //政治
        if (dto.getType() == LotteryTypeEnum.POLITICS) {
            //政治事件信息
            PoliticsLottery politicsLottery = politicsLotteryService.getById(lotteryDealer.getLotteryId());
            betOrder.setTitle(politicsLottery.getTitle());
            betOrder.setChoose(politicsLottery.findChoose(dto.getChooseNumber()));
            betOrder.setOdds(lotteryDealer.findOdds(dto.getChooseNumber()));
            betOrder.setDealerUserId(lotteryDealer.getPlayerId());
            betOrder.setDealerUsername(playerInfo.getName());
            betOrder.setDealerUserLevel(playerInfo.getLevel());
            betOrder.setAmount(amount);
            betOrder.setDrawTime(LocalDateTime.now());
            betOrder.setStatus(DrawStatusEnum.WAITING);
            this.save(betOrder);
            return;
        }
        //足球
        if (dto.getType() == LotteryTypeEnum.FOOTBALL) {

            return;
        }
    }

}
