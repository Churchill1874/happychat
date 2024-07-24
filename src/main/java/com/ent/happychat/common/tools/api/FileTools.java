package com.ent.happychat.common.tools.api;


import com.ent.happychat.common.constant.enums.FileTypeEnum;
import com.ent.happychat.common.exception.DataException;

/**
 * 文件工具
 */
public class FileTools {

    //获取文件类型
    public static FileTypeEnum getFileType(String fileExtension){
        switch (fileExtension) {
            case ".jpg":
            case ".jpeg":
            case ".png":
            case ".gif":
                return FileTypeEnum.IMAGE;
            case ".mp3":
            case ".wav":
            case ".aac":
                return FileTypeEnum.AUDIO;
            case ".mp4":
            case ".avi":
            case ".mkv":
                return FileTypeEnum.VIDEO;
            default:
                throw new DataException("请上传符合格式的文件资源");
        }
    }

}
