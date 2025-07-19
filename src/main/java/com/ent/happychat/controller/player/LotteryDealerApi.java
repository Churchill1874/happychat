package com.ent.happychat.controller.player;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.constant.enums.LotteryTypeEnum;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.LotteryDealer;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.entity.PoliticsLottery;
import com.ent.happychat.entity.view.LotteryDealerView;
import com.ent.happychat.pojo.req.lottery.dealer.DealerPageReq;
import com.ent.happychat.pojo.req.lottery.dealer.LotteryDealerAdd;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@RestController
@Api(tags = "庄家")
@RequestMapping("/player/lotteryDealer")
public class LotteryDealerApi {

    @Autowired
    private PoliticsLotteryDealerService politicsLotteryDealerService;
    @Autowired
    private LotteryDealerService lotteryDealerService;
    @Autowired
    private PoliticsLotteryService politicsLotteryService;
    @Autowired
    private PlayerInfoService playerInfoService;
    @Autowired
    private EhcacheService ehcacheService;

    @PostMapping("/queryPage")
    @ApiOperation(value = "开盘信息列表", notes = "开盘信息列表")
    public R<IPage<LotteryDealerView>> queryPage(@RequestBody @Valid DealerPageReq req) {
        if (req.getType() == LotteryTypeEnum.POLITICS) {
            IPage<LotteryDealerView> politicsLotteryDealerIPage = politicsLotteryDealerService.queryPage(req);
            return R.ok(politicsLotteryDealerIPage);
        }
        if (req.getType() == LotteryTypeEnum.FOOTBALL) {

        }
        log.error("开盘信息列表分页查询异常:{}", JSONUtil.toJsonStr(req));
        throw new DataException("开盘信息列表分页查询异常");
    }

    @PostMapping("/add")
    @ApiOperation(value = "开盘新增", notes = "开盘新增")
    public R add(@RequestBody @Valid LotteryDealerAdd req) {
        PlayerTokenResp playerTokenResp = TokenTools.getPlayerToken(true);

        Integer flag = ehcacheService.lock3SecondCache().get(playerTokenResp.getId().toString());
        if (flag != null) {
            return R.ok("您的操作过于频繁,请稍后再试");
        }
        ehcacheService.lock3SecondCache().put(playerTokenResp.getId().toString(), 1);

        PoliticsLottery politicsLottery = politicsLotteryService.getById(req.getId());
        if (politicsLottery == null) {
            throw new DataException("事件不存在");
        }

        if (LocalDateTime.now().compareTo(politicsLottery.getDrawTime()) > 0){
            throw new DataException("该事件以开奖,无法再次开盘");
        }
        if (req.getPrizePool().compareTo(new BigDecimal(100)) < 0) {
            throw new DataException("奖池不能小于 100 USDT");
        }
        if (req.getOdds1().compareTo(new BigDecimal("0.01")) < 0
            || req.getOdds2().compareTo(new BigDecimal("0.01")) < 0) {
            throw new DataException("赔率不能小于 0.01");
        }
        if (req.getOdds1().compareTo(new BigDecimal("100")) > 0
            || req.getOdds2().compareTo(new BigDecimal("100")) > 0) {
            throw new DataException("赔率不能大于 100");
        }
        if (req.getOdds1().compareTo(req.getPrizePool()) > 0
            || req.getOdds2().compareTo(req.getPrizePool()) > 0) {
            throw new DataException("赔率不能高于奖池,避免存在无法赔付下注玩家的情况");
        }

        if (StringUtils.isNotBlank(politicsLottery.getChoose3()) && req.getOdds3() == null) {
            throw new DataException("赔率不能为空");
        }
        if (StringUtils.isNotBlank(politicsLottery.getChoose3()) && req.getOdds3() != null) {
            if (req.getOdds3().compareTo(new BigDecimal("0.01")) < 0) {
                throw new DataException("赔率不能小于 0.01");
            }
            if (req.getOdds3().compareTo(new BigDecimal("100")) > 0) {
                throw new DataException("赔率不能大于 100");
            }
            if (req.getOdds3().compareTo(req.getPrizePool()) > 0) {
                throw new DataException("赔率不能高于奖池,避免存在无法赔付下注玩家的情况");
            }
        }

        LotteryDealer lotteryDealer = BeanUtil.toBean(req, LotteryDealer.class);
        lotteryDealer.setType(LotteryTypeEnum.POLITICS);
        lotteryDealer.setLotteryId(politicsLottery.getId());
        lotteryDealer.setPlayerId(playerTokenResp.getId());
        lotteryDealer.setRate1(new BigDecimal(0));
        lotteryDealer.setRate2(new BigDecimal(0));
        lotteryDealer.setCreateName(playerTokenResp.getName());

        if (StringUtils.isNotBlank(politicsLottery.getChoose3())) {
            lotteryDealer.setRate3(new BigDecimal(0));
        }

        PlayerInfo playerInfo = playerInfoService.getById(playerTokenResp.getId());
        if (playerInfo.getBalance().compareTo(lotteryDealer.getPrizePool()) < 0) {
            throw new DataException("您的余额不足");
        }
        lotteryDealerService.add(lotteryDealer);
        return R.ok(null);
    }

}
