package com.ent.happychat.service.serviceimpl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.CacheKeyConstant;
import com.ent.happychat.common.tools.TelegramBotTools;
import com.ent.happychat.common.tools.TimeUtils;
import com.ent.happychat.entity.Politics;
import com.ent.happychat.entity.PoliticsTemporary;
import com.ent.happychat.mapper.PoliticsMapper;
import com.ent.happychat.mapper.PoliticsTemporaryMapper;
import com.ent.happychat.pojo.resp.news.HomeResp;
import com.ent.happychat.service.EhcacheService;
import com.ent.happychat.service.PoliticsTemporaryService;
import org.ehcache.Cache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PoliticsTemporaryServiceImpl extends ServiceImpl<PoliticsTemporaryMapper, PoliticsTemporary> implements PoliticsTemporaryService {

    @Resource
    private EhcacheService ehcacheService;
    @Resource
    private PoliticsMapper politicsMapper;
    @Resource
    private TelegramBotTools telegramBotTools;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void moveData() {
        List<PoliticsTemporary> list = list();
        if(CollectionUtils.isEmpty(list)){
            return;
        }

        PoliticsTemporary politicsTemporary = list.get(0);
        Politics politics = BeanUtil.toBean(politicsTemporary, Politics.class);
        politics.setId(null);

        LambdaQueryWrapper<Politics> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Politics::getTitle, politics.getTitle())
                .ge(Politics::getCreateTime, TimeUtils.startOfLastNDays(7));
        if(CollectionUtils.isNotEmpty(politicsMapper.selectList(queryWrapper))){
            return;
        }

        politicsMapper.insert(politics);

        removeById(politicsTemporary.getId());

        Cache<String, HomeResp> cache = ehcacheService.homeCache();
        cache.remove(CacheKeyConstant.HOME_DATA);

        telegramBotTools.sendNews(politics.getTitle(), politics.getContent(), politics.getSource(),"国际政治",null);
    }

}
