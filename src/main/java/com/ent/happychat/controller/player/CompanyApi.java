package com.ent.happychat.controller.player;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.pojo.resp.company.CompanyResp;
import com.ent.happychat.service.CompanyService;
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
@Api(tags = "公司")
@RequestMapping("/player/company")
public class CompanyApi {

    @Autowired
    private CompanyService companyService;

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页", notes = "分页")
    public R<IPage<CompanyResp>> queryPage(@RequestBody @Valid PageBase req) {
        return R.ok(companyService.queryPageCompanyAndEvent(req));
    }


}
