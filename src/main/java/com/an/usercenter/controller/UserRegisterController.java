package com.an.usercenter.controller;

import com.an.usercenter.Exception.BusinessException;
import com.an.usercenter.common.BaseResponse;
import com.an.usercenter.common.ErrorCode;
import com.an.usercenter.common.ResultUtils;
import com.an.usercenter.model.domain.User;
import com.an.usercenter.model.domain.request.UserLoginRequest;
import com.an.usercenter.model.domain.request.UserRegisterRequest;
import com.an.usercenter.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.an.usercenter.contant.UserContant.ADMIN_ROLE;
import static com.an.usercenter.contant.UserContant.USER_LOGIN_STATE;

/**
 * @author 呼呼
 */
@RestController
@RequestMapping("/user")
public class UserRegisterController {
    @Resource
    private UserService userService;

    @PostMapping(value = "/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
//            return ResultUtils.error(ErrorCode.NULL_ERROR);
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String userAccont = userRegisterRequest.getUserAccont();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode=userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccont, userPassword, checkPassword,planetCode)) {

            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        long result = userService.userRegister(userAccont, userPassword, checkPassword, planetCode);
        return ResultUtils.success(result);
    }

    @RequestMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        String userAccont = userLoginRequest.getUserAccont();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isAnyBlank(userAccont, userPassword)) {

            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        User user = userService.doLogin(userAccont, userPassword, request);
        return ResultUtils.success(user);
    }

    @GetMapping("/current")
    public BaseResponse<User> getcurrent(HttpServletRequest request) {
        //获取登录态
        Object userObject = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currestUser = (User) userObject;
        if (currestUser == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        long userId = currestUser.getId();
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUser(String username, HttpServletRequest request) {
        //鉴权 仅仅管理员可查询
        //如果不是管理员 false
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
            //new ArrayList<>() 空的意思，这时候返回给前端session是空对象
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> collect = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(collect);
//        return  userList.stream().map(user -> {
//            return userService.getSafetyUser(user);
//        }).collect(Collectors.toList());
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return ResultUtils.error(ErrorCode.NO_AUTH);
            //new ArrayList<>() 空的意思，这时候返回给前端session是空对象
        }

        isAdmin(request);
        if (id <= 0) {
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        boolean remove = userService.removeById(id);
        return ResultUtils.success(remove);
    }

    /**
     * 用户注销
     * @param request
     */
    @PostMapping("logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request==null){
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        int i = userService.userLogin(request);
        return ResultUtils.success(i);

    }

    /**
     * 是否为管理员
     *
     * @param request 请求
     * @return ture
     */
    private boolean isAdmin(HttpServletRequest request) {
        //鉴权 仅仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        //简写
        // return user != null && user.getUserRole() == ADMIN_ROLE;
        //不简写
        if ((user == null) || (user.getUserRole() != ADMIN_ROLE)) {
            return false;
        }
        return true;

    }


}
