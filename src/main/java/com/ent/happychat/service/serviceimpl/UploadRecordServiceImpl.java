package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.enums.FileTypeEnum;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.entity.UploadRecord;
import com.ent.happychat.mapper.UploadRecordMapper;
import com.ent.happychat.service.UploadRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class UploadRecordServiceImpl extends ServiceImpl<UploadRecordMapper, UploadRecord> implements UploadRecordService {

    @Override
    public void cleanNotUsedFile() {
        //获取所有超过24小时未使用的文件
        QueryWrapper<UploadRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().le(UploadRecord::getCreateTime, LocalDateTime.now().minusHours(24));
        List<UploadRecord> list = list(queryWrapper);
        if (CollectionUtils.isEmpty(list)){
            return;
        }

        //遍历应该删除的未使用上传的资源
        for(UploadRecord uploadRecord: list){
            try {
                String filePath = uploadRecord.getPath();
                Path path = Paths.get(filePath);
                boolean result = Files.deleteIfExists(path);

                //如果删除成功
                if (result){
                    removeById(uploadRecord.getId());
                }
            } catch (IOException e) {
                log.error("删除未使用文件资源异常:{}", e.getMessage());
            }
        }
    }

    @Override
    public void insertRecord(String path, FileTypeEnum fileTypeEnum, String createName) {

        log.info("插入图片记录path:{},fileType:{},createName:{}", path, fileTypeEnum, createName);
        UploadRecord uploadRecord = new UploadRecord();
        uploadRecord.setPath(path);
        uploadRecord.setFileType(fileTypeEnum);
        uploadRecord.setCreateName(createName);
        uploadRecord.setCreateTime(LocalDateTime.now());
        save(uploadRecord);
    }

    @Override
    public void cleanByPath(String path) {
        QueryWrapper<UploadRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UploadRecord::getPath, path);
        remove(queryWrapper);
    }

    @Override
    public void cleanRemoveFile(String path) {
        Path filePath = Paths.get(path);
        boolean result = false;
        try {
            result = Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //如果删除成功
        if (!result){
            throw new DataException("删除文件异常");
        }
    }

}
