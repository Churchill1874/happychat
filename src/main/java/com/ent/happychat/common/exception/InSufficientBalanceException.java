package com.ent.happychat.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InSufficientBalanceException extends RuntimeException{
    private Integer code = -6;

    private String message = "余额不足";

    public InSufficientBalanceException(String message) {
        this.message = message;
    }

}
