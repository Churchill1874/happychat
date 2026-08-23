// 目标路径: src/main/java/com/ent/happychat/service/serviceimpl/TelegramServiceImpl.java
package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.Telegram;
import com.ent.happychat.mapper.TelegramMapper;
import com.ent.happychat.pojo.req.telegram.TelegramPageReq;
import com.ent.happychat.service.TelegramService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TelegramServiceImpl extends ServiceImpl<TelegramMapper, Telegram> implements TelegramService {

    @Override
    public IPage<Telegram> queryPage(TelegramPageReq req) {
        IPage<Telegram> iPage = new Page<>(req.getPageNum(), req.getPageSize());

        QueryWrapper<Telegram> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(req.getId() != null, Telegram::getId, req.getId())
                .eq(req.getType() != null, Telegram::getType, req.getType())
                .eq(req.getIsTop() != null, Telegram::getIsTop, req.getIsTop())
                .eq(req.getStatus() != null, Telegram::getStatus, req.getStatus())
                .eq(StringUtils.isNotBlank(req.getAccount()), Telegram::getAccount, req.getAccount())
                .like(StringUtils.isNotBlank(req.getTitle()), Telegram::getTitle, req.getTitle())
                .orderByDesc(Telegram::getIsTop)
                .orderByDesc(Telegram::getCreateTime);

        return page(iPage, queryWrapper);
    }

    @Override
    public void add(Telegram telegram) {
        if (StringUtils.isBlank(telegram.getTitle())) {
            throw new DataException("标题不能为空");
        }
        if (telegram.getType() == null) {
            throw new DataException("类型不能为空");
        }
        if (StringUtils.isBlank(telegram.getAccount())) {
            throw new DataException("Telegram账号不能为空");
        }
        if (StringUtils.isBlank(telegram.getJumpUrl())) {
            throw new DataException("跳转链接不能为空");
        }
        if (StringUtils.isBlank(telegram.getPosterImagePath())) {
            throw new DataException("海报图片不能为空");
        }
        if (StringUtils.isBlank(telegram.getDescription())) {
            throw new DataException("描述内容不能为空");
        }
        if (telegram.getIsTop() == null) {
            telegram.setIsTop(false);
        }
        if (telegram.getStatus() == null) {
            telegram.setStatus(true);
        }
        telegram.setCreateTime(LocalDateTime.now());
        telegram.setCreateName(TokenTools.getAdminName());
        save(telegram);
    }

}
