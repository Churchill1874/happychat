package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.common.constant.enums.FileTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("upload_record")
public class UploadRecord implements Serializable {
    private static final long serialVersionUID = 7903809383072409327L;

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @TableField("path")
    @ApiModelProperty("路径")
    private String path;

    @TableField("file_type")
    @ApiModelProperty("文件类型")
    private FileTypeEnum fileType;

    @TableField("create_name")
    @ApiModelProperty("创建人")
    private String createName;

    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}