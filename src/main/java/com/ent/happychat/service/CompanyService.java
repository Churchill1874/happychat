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
    IPage<CompanyResp> queryPageCompanyAndEvent(PageBase po);

    /**
     * 最新公司
     * @return
     */
    CompanyResp last();

    /**
     * 分页公司
     * @param po
     * @return
     */
    IPage<Company> queryPage(PageBase po);
}
