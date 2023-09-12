package com.ent.happychat.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountOrPasswordException extends RuntimeException{

    private Integer code = -4;

    private String message = "账号或密码有误";

}
