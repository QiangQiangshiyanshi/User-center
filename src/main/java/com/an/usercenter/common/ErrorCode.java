package com.an.usercenter.common;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 * 错误码
 *
 * @author 呼呼
 */
@Getter
public enum ErrorCode {
    SUCCESS(0, "ok", ""),
    PARAMS_ERROR(40000, "请求参数错误", ""),
    NULL_ERROR(40001, "数据为空", ""),
    NO_LOGIN(40101, "未登录", ""),
    NO_AUTH(40100, "无权限", ""),
    SYSTEM_ERROR(50000, "系统内部异常", "");

    /**
     * 状态码
     */
    private final int code;
    /**
     * 状态码信息
     */
    private final String message;
    /**
     * 状态码详情
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }


}
