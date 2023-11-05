package com.ent.happychat.common.exception;

import com.baomidou.mybatisplus.extension.api.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TokenException.class)
    public R errorTokenExceptionHandler(TokenException e) {
        log.error("未登录操作:{}", e.getMessage());
        return R.failed(e.getMessage()).setCode(e.getCode());
    }
    @ExceptionHandler(AuthException.class)
    public R errorAuthExceptionHandler(AuthException e) {
        log.error("未授权操作:{}", e.getMessage());
        return R.failed(e.getMessage()).setCode(e.getCode());
    }
    @ExceptionHandler(AccountOrPasswordException.class)
    public R errorAccountOrPasswordException(AccountOrPasswordException e) {
        log.error("账号或密码有误:{}", e.getMessage());
        return R.failed(e.getMessage()).setCode(e.getCode());
    }
    @ExceptionHandler(IpException.class)
    public R errorIpException(IpException e) {
        log.error("获取ip失败:{}", e.getMessage());
        return R.failed(e.getMessage()).setCode(e.getCode());
    }
    @ExceptionHandler(DataException.class)
    public R errorDataException(DataException e) {
        log.error("业务异常:{}", e.getMessage());
        return R.failed(e.getMessage()).setCode(e.getCode());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R errorMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error("数据异常:{}", e.getFieldError().getDefaultMessage());
        return R.failed(e.getBindingResult().getFieldError().getDefaultMessage()).setCode(-1);
    }
    @ExceptionHandler(Exception.class)
    public R errorExceptionHandler(Exception e) {
        log.error("异常信息:", e.getMessage());
        return R.failed(e.toString()).setCode(-1);
    }

}
