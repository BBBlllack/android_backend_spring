package com.shj.apiserver.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shj.apiserver.entity.User;
import com.shj.apiserver.mapper.UserMapper;
import com.shj.apiserver.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
