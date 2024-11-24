package com.ent.happychat.controller.player;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.entity.SoutheastAsia;
import com.ent.happychat.pojo.req.southeastasia.SoutheastAsiaPageReq;
import com.ent.happychat.service.SoutheastAsiaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(tags = "东南亚新闻")
@RequestMapping("/player/southeastAsia/")
public class SoutheastAsiaApi {

    @Autowired
    private SoutheastAsiaService southeastAsiaService;

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<IPage<SoutheastAsia>> queryPage(SoutheastAsiaPageReq req) {
        IPage<SoutheastAsia> iPage = southeastAsiaService.queryPage(req);
        return R.ok(iPage);
    }

}
