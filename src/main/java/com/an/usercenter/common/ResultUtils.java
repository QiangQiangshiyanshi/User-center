package com.an.usercenter.common;

/**
 * 返回工具类
 */
public class ResultUtils {
    /**
     * 成功
     *
     * @param date
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T date) {
        return new BaseResponse<>(0, date, "ok", "");
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
//    public static BaseResponse error(ErrorCode errorCode){
//        return new BaseResponse<>(errorCode.getCode(),null,errorCode.getMessage(),errorCode.getDescription());
//    }
    public static BaseResponse error(ErrorCode errorCode, String massage, String description) {
        return new BaseResponse(errorCode.getCode(),null ,massage, description);

    }

    public static BaseResponse error(ErrorCode errorCode, String description) {
        return new BaseResponse(errorCode.getCode(),null, errorCode.getMessage(), description);

    }

    public static BaseResponse error(int code, String massage, String description) {
        return new BaseResponse(code, null,massage, description);

    }


}
