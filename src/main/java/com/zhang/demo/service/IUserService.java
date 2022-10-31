package com.zhang.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhang.demo.common.Result;
import com.zhang.demo.dto.LoginForm;
import com.zhang.demo.entity.User;



public interface IUserService extends IService<User> {

    Result login(LoginForm loginForm);

}
