package com.ent.happychat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.common.constant.enums.FileTypeEnum;
import com.ent.happychat.entity.UploadRecord;

public interface UploadRecordService extends IService<UploadRecord> {

    /**
     * 删除未使用的文件
     */
    void cleanNotUsedFile();

    /**
     * 上传文件的暂时未使用记录
     * @param path
     * @param fileTypeEnum
     * @param createName
     */
    void insertRecord(String path, FileTypeEnum fileTypeEnum, String createName);

    /**
     * 根据文件路径删除记录
     * @param path
     */
    void cleanByPath(String path);
}
