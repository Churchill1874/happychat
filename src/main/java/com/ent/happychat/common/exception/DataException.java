package com.ent.happychat.common.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DataException extends RuntimeException {

    private Integer code = -1;

    private String message = "数据异常";

    public DataException(String message) {
        this.message = message;
    }

}
