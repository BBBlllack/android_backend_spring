package com.shj.apiserver.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shj.apiserver.entity.Paper;
import com.shj.apiserver.mapper.PaperMapper;
import com.shj.apiserver.service.PaperService;
import org.springframework.stereotype.Service;

@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper, Paper> implements PaperService {
}
