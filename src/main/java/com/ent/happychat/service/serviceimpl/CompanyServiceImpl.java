package com.ent.happychat.service.serviceimpl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.entity.Company;
import com.ent.happychat.entity.CompanyEvent;
import com.ent.happychat.mapper.CompanyMapper;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.pojo.resp.company.CompanyResp;
import com.ent.happychat.service.CompanyEventService;
import com.ent.happychat.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {
    @Autowired
    private CompanyEventService companyEventService;

    @Override
    public IPage<CompanyResp> queryPage(PageBase po) {
        IPage<CompanyResp> companyRespPage = new Page<>(po.getPageNum(), po.getPageSize());

        IPage<Company> iPage = new Page<>(po.getPageNum(), po.getPageSize());
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(Company::getCreateTime);
        iPage = page(iPage, queryWrapper);

        if (CollectionUtils.isEmpty(iPage.getRecords())){
            return companyRespPage;
        }

        List<Long> companyIdList = new ArrayList<>();
        List<CompanyResp> companyRespList = new ArrayList<>();
        iPage.getRecords().forEach(company -> {
            CompanyResp companyResp = BeanUtil.toBean(company, CompanyResp.class);
            companyRespList.add(companyResp);
            companyIdList.add(company.getId());
        });
        companyRespPage.setRecords(companyRespList);


        Map<Long,List<CompanyEvent>> companyEventMap = companyEventService.mapGroup(companyIdList);
        if (CollectionUtils.isEmpty(companyEventMap)){
            return companyRespPage;
        }
        companyRespPage.getRecords().forEach(companyResp -> {
            companyResp.setCompanyEventList(companyEventMap.get(companyResp.getId()));
        });

        return companyRespPage;
    }
}
