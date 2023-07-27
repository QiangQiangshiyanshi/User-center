package com.an.usercenter.service;
import java.util.Date;

import com.an.usercenter.model.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.an.usercenter.mapper.UserMapper;
import org.junit.Assert;
import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Resource
    UserService userService;
    @Test
    public void testAddUser(){
        String userAccont ="yupi9";
        String userPassword="123456789";
        String checkPassword="123456789";
        String planetCode="";
        long result = userService.userRegister(userAccont, userPassword, checkPassword,planetCode);


}

}
