package com.zhang.demo.controller;


import com.zhang.demo.common.Result;
import com.zhang.demo.dto.LoginForm;
import com.zhang.demo.service.IUserService;
import com.zhang.demo.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private IUserService iUserService;

    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm) {


        return iUserService.login(loginForm);
    }
}
