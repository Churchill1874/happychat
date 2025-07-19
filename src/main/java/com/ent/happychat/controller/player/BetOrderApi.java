package com.ent.happychat.controller.player;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.BetOrder;
import com.ent.happychat.entity.view.LotteryDealerView;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.pojo.req.betorder.BetOrderAddReq;
import com.ent.happychat.pojo.req.betorder.BetOrderPageReq;
import com.ent.happychat.pojo.req.lottery.dealer.DealerPageReq;
import com.ent.happychat.service.BetOrderService;
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
@Api(tags = "下注订单")
@RequestMapping("/player/betOrder")
public class BetOrderApi {

    @Autowired
    private BetOrderService betOrderService;

    @PostMapping("/playerPage")
    @ApiOperation(value = "分页个人注单", notes = "分页个人注单")
    public R<IPage<BetOrder>> playerPage(@RequestBody @Valid BetOrderPageReq req) {
        req.setPlayerId(TokenTools.getPlayerToken(true).getId());
        return R.ok(betOrderService.queryPage(req));
    }

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页公共注单", notes = "分页公共注单")
    public R<IPage<BetOrder>> queryPage(@RequestBody @Valid BetOrderPageReq req) {
        return R.ok(betOrderService.queryPage(req));
    }

    @PostMapping("/betPolitics")
    @ApiOperation(value = "政治下注", notes = "分页公共注单")
    public R betPolitics(@RequestBody @Valid BetOrderAddReq req) {
        betOrderService.bet(req);
        return R.ok(null);
    }

}
