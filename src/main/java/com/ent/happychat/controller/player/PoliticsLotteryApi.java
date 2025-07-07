package com.ent.happychat.controller.player;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.entity.PoliticsLottery;
import com.ent.happychat.pojo.req.lottery.dealer.DealerPageReq;
import com.ent.happychat.service.PoliticsLotteryService;
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
@Api(tags = "政治彩票")
@RequestMapping("/player/politicsLottery")
public class PoliticsLotteryApi {

    @Autowired
    private PoliticsLotteryService politicsLotteryService;

    @PostMapping("/queryPage")
    @ApiOperation(value = "政治彩票信息列表", notes = "政治彩票信息列表")
    public R<IPage<PoliticsLottery>> queryPage(@RequestBody @Valid DealerPageReq req) {
        IPage<PoliticsLottery> iPage = politicsLotteryService.queryPage(req);
        return R.ok(iPage);
    }

}
