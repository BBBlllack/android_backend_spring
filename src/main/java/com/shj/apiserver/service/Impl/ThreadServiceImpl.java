package com.shj.apiserver.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shj.apiserver.entity.Thread;
import com.shj.apiserver.mapper.ThreadMapper;
import com.shj.apiserver.service.ThreadService;
import org.springframework.stereotype.Service;

@Service
public class ThreadServiceImpl extends ServiceImpl<ThreadMapper, Thread> implements ThreadService {
}
