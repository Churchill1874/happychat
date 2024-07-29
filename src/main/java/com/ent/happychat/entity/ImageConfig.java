package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.common.constant.enums.ImageTypeEnum;
import com.ent.happychat.entity.base.UpdateBaseInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("image_config")
public class ImageConfig extends UpdateBaseInfo implements Serializable {
    private static final long serialVersionUID = 3482537421569484234L;

    @ApiModelProperty("图片类型")
    @TableField("image_type")
    private ImageTypeEnum imageType;

    @ApiModelProperty("路径")
    @TableField("path")
    private String path;

    @ApiModelProperty("排序")
    @TableField("sort")
    private Integer sort;

    @ApiModelProperty("使用状态")
    @TableField("status")
    private Boolean status;

}
