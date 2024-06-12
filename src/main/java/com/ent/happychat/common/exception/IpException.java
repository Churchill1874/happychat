package com.ent.happychat.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IpException extends RuntimeException{

    private Integer code = -5;

    private String message = "异常ip";

    public IpException(String message) {
        this.message = message;
    }
}
