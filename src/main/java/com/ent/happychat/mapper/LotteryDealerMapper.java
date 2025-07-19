package com.ent.happychat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ent.happychat.entity.LotteryDealer;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface LotteryDealerMapper extends BaseMapper<LotteryDealer> {

    //减少奖池金额
    void reduceRemainingPricePool(@Param("amount")BigDecimal amount, @Param("id")Long id);

}
