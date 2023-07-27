package com.an.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author 呼呼
 */
@Data
public class UserLoginRequest implements Serializable {
    private String userAccont;
    private String userPassword;
    private String checkPassword;

}
