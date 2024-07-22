package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.entity.ImageConfig;
import com.ent.happychat.mapper.ImageConfigMapper;
import com.ent.happychat.pojo.req.image.ImageConfigPageReq;
import com.ent.happychat.service.ImageConfigService;
import com.ent.happychat.service.UploadRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ImageConfigServiceImpl extends ServiceImpl<ImageConfigMapper, ImageConfig> implements ImageConfigService {
    @Autowired
    private UploadRecordService uploadRecordService;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(ImageConfig imageConfig) {
        uploadRecordService.cleanByPath(imageConfig.getPath());
        save(imageConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        ImageConfig imageConfig = getById(id);
        if (imageConfig == null){
            throw new DataException("图片未找到");
        }
        uploadRecordService.cleanRemoveFile(imageConfig.getPath());
        removeById(id);
    }


}
