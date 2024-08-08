package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.common.constant.enums.FileTypeEnum;
import com.ent.happychat.entity.base.BaseInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("upload_record")
public class UploadRecord extends BaseInfo implements Serializable {
    private static final long serialVersionUID = 7903809383072409327L;

    @ApiModelProperty("路径")
    private String path;

    @ApiModelProperty("文件类型")
    private FileTypeEnum fileType;

}