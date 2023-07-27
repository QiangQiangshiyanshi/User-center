package com.an.usercenter.Exception;

import com.an.usercenter.common.BaseResponse;
import com.an.usercenter.common.ErrorCode;
import com.an.usercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author 呼呼
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessException(BusinessException e) {
        //出现异常就纪录
        log.error(e.getMessage()+"runtime_Exception",e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage(), e.getDescription());

    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeException(RuntimeException r) {
        //出现异常就纪录
        log.error("runtime_Exception",r);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,r.getMessage(),"");
    }
}
