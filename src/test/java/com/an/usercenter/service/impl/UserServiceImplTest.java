package com.an.usercenter.service.impl;

import com.an.usercenter.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {
    @Resource
    UserService userService;
@Test
    void userRegister(){
    String userAccont ="yupi9";
    String userPassword="123456789";
    String checkPassword="123456789";
    String planetCode="";
   long result = userService.userRegister(userAccont, userPassword, checkPassword,planetCode);
   assertEquals(-1,result);
//    userAccont ="yu";
//    result = userService.userRegister(userAccont, userPassword, checkPassword);
//    assertEquals(-1,result);
//    userAccont ="yupi";
//    userPassword="123456";
//    result = userService.userRegister(userAccont, userPassword, checkPassword);
//    assertEquals(-1,result);
//    userAccont ="yu pi";
//     userPassword="12345678";
//    result = userService.userRegister(userAccont, userPassword, checkPassword);
//    assertEquals(-1,result);
//    checkPassword="123456789";
//    result = userService.userRegister(userAccont, userPassword, checkPassword);
//    assertEquals(-1,result);
//    userAccont ="yupi";
//    checkPassword="12345678";
//    result = userService.userRegister(userAccont, userPassword, checkPassword);
//    assertEquals(-1,result);
//    userAccont ="Yupi";
//    assertEquals(-1,result);




}
}
