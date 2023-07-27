package com.an.usercenter.service;

import com.an.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 呼呼
 * 用户服务
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2023-07-11 15:36:08
 */

public interface UserService extends IService<User> {


    /**
     * 用户注册
     *
     * @param userAccont    用户账号
     * @param userPassword  用户密码
     * @param checkPassword 用户校验
     * @param planetCode 星球编号
     * @return 用户Id
     */
    long userRegister(String userAccont, String userPassword, String checkPassword,String planetCode);

    /**
     * 用户登录
     *
     * @param userAccont   用户账户
     * @param userPassword 用户密码
     * @param request      HTTp
     * @return 脱敏后的用户信息
     */
    User doLogin(String userAccont, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */

    User getSafetyUser(User originUser);

    int userLogin(HttpServletRequest request);
}

