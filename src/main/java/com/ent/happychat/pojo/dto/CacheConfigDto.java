package com.ent.happychat.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CacheConfigDto implements Serializable {
    private static final long serialVersionUID = -3994163539371761595L;

    //在线人数
    private int playerOnlineCount = 0;

}
