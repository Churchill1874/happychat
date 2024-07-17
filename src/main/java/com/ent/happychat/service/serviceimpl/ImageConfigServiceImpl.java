package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.entity.ImageConfig;
import com.ent.happychat.mapper.ImageConfigMapper;
import com.ent.happychat.pojo.req.image.ImageConfigPageReq;
import com.ent.happychat.service.ImageConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ImageConfigServiceImpl extends ServiceImpl<ImageConfigMapper, ImageConfig> implements ImageConfigService {

    @Override
    public IPage<ImageConfig> queryPage(ImageConfigPageReq po) {
        IPage<ImageConfig> iPage = new Page<>(po.getPageNum(), po.getPageSize());
        QueryWrapper<ImageConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(ImageConfig::getStatus, po.getStatus())
            .eq(ImageConfig::getImageType, po.getImageType())
            .orderByDesc(ImageConfig::getSort).orderByDesc(ImageConfig::getCreateTime);
        return page(iPage, queryWrapper);
    }


}
