package com.an.usercenter.service.impl;

import com.an.usercenter.Exception.BusinessException;
import com.an.usercenter.common.ErrorCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.an.usercenter.model.domain.User;
import com.an.usercenter.service.UserService;
import com.an.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.an.usercenter.contant.UserContant.USER_LOGIN_STATE;

/**
 * @author 呼呼
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2023-07-11 15:36:08
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Resource
    UserMapper userMapper;
    /**
     * SALT 盐值
     */
    private static final String SALT = "yupi";

    @Override
    public long userRegister(String userAccont, String userPassword, String checkPassword, String planetCode) {
        //1、校验
        if (StringUtils.isAnyBlank(userAccont, userPassword, checkPassword, planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if (userAccont.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"数值错误");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"数值错误");

        }
        if (planetCode.length() > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"数值错误");
        }
        //账户不能包含特殊字符

        String valicateAccount = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        //matcher()括号里就是需要校验的字符
        Matcher matcher = Pattern.compile(valicateAccount).matcher(userAccont);
        //matches() 方法用于检测字符串是否匹配给定的正则表达式。
        boolean matches = matcher.matches();
        //如果找到
//        if (matches == false) {
//            return -1;
//        }
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"字符错误");
        }

        //密码和校验密码不相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码和校验密码不相同");
        }

        //账户不能重复
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccont", userAccont);
        //在数据库中查询queryWrapper数量
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"存在数据");
        }
        //星球账户不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode", planetCode);
        //在数据库中查询queryWrapper数量
        long count1 = userMapper.selectCount(queryWrapper);
        if (count1 > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球数据存在");
        }

        //2.对代码进行加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        System.out.println(encryptPassword);

        //3、插入数据
        User user = new User();
        user.setUserAccont(userAccont);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        Boolean saveResule = this.save(user);
        if (!saveResule) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"保存失败");
        }

        return user.getId();
    }

    @Override
    public User doLogin(String userAccont, String userPassword, HttpServletRequest request) {
        //1、校验
        if (StringUtils.isAnyBlank(userAccont, userPassword)) {
            //todo 修改为字定义异常
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if (userAccont.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"数值错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"数值错误");

        }
        //账户不能包含特殊字符
        String valicateAccount = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        //matcher()括号里就是需要校验的字符
        Matcher matcher = Pattern.compile(valicateAccount).matcher(userAccont);
        //matches() 方法用于检测字符串是否匹配给定的正则表达式。
        boolean matches = matcher.matches();
        //如果找到
//        if (matches == false) {
//            return null;
//        }
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"字符错误");

        }
        // 2.对代码进行加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccont", userAccont);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);


        //用户不存在
        if (user == null) {
            log.info("user login faild,userAccout cannot match userPassword");
            throw new BusinessException(ErrorCode.NO_LOGIN,"用户不存在");

        }
        //3、用户脱敏

        User safeUser = getSafetyUser(user);

        //4、记录用户的用户态   setAttribute以Map键值对的方式存储 key=USER_LOGIN_STATE = "userLoginState"，value=safeUser
        request.getSession().setAttribute(USER_LOGIN_STATE, safeUser);
        return safeUser;

    }

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"用户不存在");
        }
        User safeUser = new User();
        safeUser.setId(originUser.getId());
        safeUser.setUsername(originUser.getUsername());
        safeUser.setUserAccont(originUser.getUserAccont());
        safeUser.setAvatarUrl(originUser.getAvatarUrl());
        safeUser.setGender(originUser.getGender());
        safeUser.setEmail(originUser.getEmail());
        safeUser.setUserRole(originUser.getUserRole());
        safeUser.setUserStatus(originUser.getUserStatus());
        safeUser.setPhone(originUser.getPhone());
        safeUser.setCreateTime(originUser.getCreateTime());
        safeUser.setPlanetCode(originUser.getPlanetCode());
        return safeUser;
    }

    @Override
    public int userLogin(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }


}




