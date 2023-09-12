package com.ent.happychat.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthException extends RuntimeException {

    private Integer code = -3;

    private String message = "操作权限不足";

}
