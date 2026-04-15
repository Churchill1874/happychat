package com.ent.happychat.common.exception;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.tools.HttpTools;
import com.ent.happychat.entity.LogInfo;
import com.ent.happychat.service.LogInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private LogInfoService logInfoService;

    private void insertInfo(String content){
        LogInfo logInfo = new LogInfo();
        logInfo.setType(1);
        logInfo.setContent(content);
        logInfo.setIp(HttpTools.getIp());
        logInfo.setAddress(HttpTools.getAddress());
        logInfo.setCreateTime(LocalDateTime.now());
        logInfo.setCreateName("系统");
        logInfoService.asyncSave(logInfo);
    }

    @ExceptionHandler(InSufficientBalanceException.class)
    public R errorInSufficientBalanceHandler(InSufficientBalanceException e){
        log.error("钱包异常:{}",e.getMessage());
        insertInfo("钱包异常:" + e.getMessage());
        return R.failed(e.getMessage()).setCode(e.getCode());
    }

    @ExceptionHandler(TokenException.class)
    public R errorTokenExceptionHandler(TokenException e) {
        log.error("未登录操作:{}", e.getMessage());
        insertInfo("未登录操作:" + e.getMessage());
        return R.failed(e.getMessage()).setCode(e.getCode());
    }
    @ExceptionHandler(AuthException.class)
    public R errorAuthExceptionHandler(AuthException e) {
        log.error("未授权操作:{}", e.getMessage());
        insertInfo("未授权操作:" + e.getMessage());
        return R.failed(e.getMessage()).setCode(e.getCode());
    }
    @ExceptionHandler(AccountOrPasswordException.class)
    public R errorAccountOrPasswordException(AccountOrPasswordException e) {
        log.error("账号或密码有误:{}", e.getMessage());
        insertInfo("账号或密码有误:" + e.getMessage());
        return R.failed(e.getMessage()).setCode(e.getCode());
    }
    @ExceptionHandler(IpException.class)
    public R errorIpException(IpException e) {
        log.error("ip异常:{}", e.getMessage());
        insertInfo("ip异常:" + e.getMessage());
        return R.failed(e.getMessage()).setCode(e.getCode());
    }
    @ExceptionHandler(DataException.class)
    public R errorDataException(DataException e) {
        log.error("业务异常:{}", e.getMessage());
        insertInfo("业务异常:" + e.getMessage());
        return R.failed(e.getMessage()).setCode(e.getCode());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R errorMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error("数据异常:{}", e.getFieldError().getDefaultMessage());
        insertInfo("数据异常:" + e.getMessage());
        return R.failed(e.getBindingResult().getFieldError().getDefaultMessage()).setCode(-1);
    }
    @ExceptionHandler(Exception.class)
    public R errorExceptionHandler(Exception e) {
        //e.printStackTrace();
        log.error("系统异常信息:", e.getMessage());
        insertInfo("系统异常信息:" + e.getMessage());
        return R.failed(e.toString()).setCode(-1);
    }

}
