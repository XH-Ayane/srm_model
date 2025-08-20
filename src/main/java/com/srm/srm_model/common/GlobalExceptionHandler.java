package com.srm.srm_model.common;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 处理所有未捕获的异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<?> handleException(Exception e) {
        return Result.fail(500, "服务器内部错误: " + e.getMessage());
    }

    // 处理自定义异常
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public Result<?> handleBusinessException(BusinessException e) {
        return Result.fail(e.getCode(), e.getMessage());
    }
}