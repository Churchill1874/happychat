package com.ent.happychat.service.serviceimpl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.tools.TelegramBotTools;
import com.ent.happychat.entity.Politics;
import com.ent.happychat.entity.PoliticsTemporary;
import com.ent.happychat.mapper.PoliticsMapper;
import com.ent.happychat.mapper.PoliticsTemporaryMapper;
import com.ent.happychat.service.PoliticsTemporaryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PoliticsTemporaryServiceImpl extends ServiceImpl<PoliticsTemporaryMapper, PoliticsTemporary> implements PoliticsTemporaryService {

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

        politicsMapper.insert(politics);

        removeById(politicsTemporary.getId());

        telegramBotTools.sendNews(politics.getTitle(), politics.getContent(), politics.getSource(),"国际政治",null);
    }

}
