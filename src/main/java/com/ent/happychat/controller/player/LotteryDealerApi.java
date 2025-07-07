package com.ent.happychat.controller.player;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.constant.enums.LotteryTypeEnum;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.entity.view.LotteryDealerView;
import com.ent.happychat.pojo.req.lottery.dealer.DealerPageReq;
import com.ent.happychat.service.PoliticsLotteryDealerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@Api(tags = "庄家")
@RequestMapping("/player/lotteryDealer")
public class LotteryDealerApi {

    @Autowired
    private PoliticsLotteryDealerService politicsLotteryDealerService;

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


}
