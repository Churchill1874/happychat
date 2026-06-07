package com.ent.happychat.service.serviceimpl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.CacheKeyConstant;
import com.ent.happychat.common.tools.TelegramBotTools;
import com.ent.happychat.common.tools.TimeUtils;
import com.ent.happychat.entity.Politics;
import com.ent.happychat.entity.SoutheastAsia;
import com.ent.happychat.entity.SoutheastAsiaTemporary;
import com.ent.happychat.mapper.SoutheastAsiaMapper;
import com.ent.happychat.mapper.SoutheastAsiaTemporaryMapper;
import com.ent.happychat.pojo.resp.news.HomeResp;
import com.ent.happychat.service.EhcacheService;
import com.ent.happychat.service.SoutheastAsiaTemporaryService;
import org.ehcache.Cache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SoutheastAsiaTemporaryServiceImpl extends ServiceImpl<SoutheastAsiaTemporaryMapper, SoutheastAsiaTemporary> implements SoutheastAsiaTemporaryService {

    @Resource
    private EhcacheService ehcacheService;
    @Resource
    private SoutheastAsiaMapper southeastAsiaMapper;
    @Resource
    private TelegramBotTools telegramBotTools;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void moveData() {
        List<SoutheastAsiaTemporary> list = list();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        SoutheastAsiaTemporary southeastAsiaTemporary = list.get(0);
        SoutheastAsia southeastAsia = BeanUtil.toBean(southeastAsiaTemporary, SoutheastAsia.class);
        southeastAsia.setId(null);

        LambdaQueryWrapper<SoutheastAsia> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(SoutheastAsia::getTitle, southeastAsia.getTitle())
                .ge(SoutheastAsia::getCreateTime, TimeUtils.startOfLastNDays(7));

        if(CollectionUtils.isNotEmpty(southeastAsiaMapper.selectList(queryWrapper))){
            removeById(southeastAsiaTemporary.getId());
            return;
        }

        southeastAsiaMapper.insert(southeastAsia);

        removeById(southeastAsiaTemporary.getId());

        Cache<String, HomeResp> cache = ehcacheService.homeCache();
        cache.remove(CacheKeyConstant.HOME_DATA);

        telegramBotTools.sendNews(southeastAsiaTemporary.getTitle(), southeastAsiaTemporary.getContent(), southeastAsiaTemporary.getSource(),"东南亚新闻",null);
    }

}
