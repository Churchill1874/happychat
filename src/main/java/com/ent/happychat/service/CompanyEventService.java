package com.ent.happychat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.CompanyEvent;

import java.util.List;
import java.util.Map;

public interface CompanyEventService extends IService<CompanyEvent> {

    /**
     * 查询公司列表的事件列表
     * @param companyIdList
     * @return
     */
    Map<Long, List<CompanyEvent>> mapGroup(List<Long> companyIdList);

}
