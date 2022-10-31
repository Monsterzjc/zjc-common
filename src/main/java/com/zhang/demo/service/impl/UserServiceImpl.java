package com.zhang.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.demo.common.Constants;
import com.zhang.demo.common.Result;
import com.zhang.demo.dto.LoginForm;
import com.zhang.demo.dto.UserDTO;
import com.zhang.demo.entity.User;
import com.zhang.demo.mapper.UserMapper;
import com.zhang.demo.service.IUserService;

import org.omg.CORBA.ObjectHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.zhang.demo.common.RedisConstants.LOGIN_USER_KEY;
import static com.zhang.demo.common.RedisConstants.LOGIN_USER_TTL;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result login(LoginForm loginForm) {

        if (loginForm == null) {
            return Result.error(Constants.CODE_600,"登录失败");
        }

        String username = loginForm.getUsername();
        String password = loginForm.getPassword();

        User user = getOne(new QueryWrapper<User>().eq("phone",username));



        if (user == null || !user.getPassword().equals(password)) {
            System.out.println("user"+user);
            return Result.error(Constants.CODE_600,"用户名或密码错误");
        }

        String token = UUID.randomUUID().toString();
        String tokenKey = LOGIN_USER_KEY + token;
        System.out.println(tokenKey);
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);

        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                CopyOptions.create()
                    .setIgnoreNullValue(true)
                    .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));
        stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
        stringRedisTemplate.expire(tokenKey, LOGIN_USER_TTL, TimeUnit.MINUTES);
        return Result.success(token);
    }
}
