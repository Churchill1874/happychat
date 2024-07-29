package com.ent.happychat.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UpdateBaseInfo extends BaseInfo implements Serializable {
    private static final long serialVersionUID = 6722872925157116023L;

    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @TableField("update_name")
    private String updateName;

}
