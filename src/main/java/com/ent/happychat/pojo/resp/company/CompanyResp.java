package com.ent.happychat.pojo.resp.company;

import com.ent.happychat.entity.Company;
import com.ent.happychat.entity.CompanyEvent;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CompanyResp extends Company implements Serializable {
    private static final long serialVersionUID = 3651805820434521585L;

    @ApiModelProperty("事件记录列表")
    List<CompanyEvent> companyEventList;

}
