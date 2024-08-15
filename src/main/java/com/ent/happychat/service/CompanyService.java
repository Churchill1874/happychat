package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.Company;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.pojo.resp.company.CompanyResp;

public interface CompanyService extends IService<Company> {

    /**
     * 分页
     * @param po
     * @return
     */
    IPage<CompanyResp> queryPage(PageBase po);

}
