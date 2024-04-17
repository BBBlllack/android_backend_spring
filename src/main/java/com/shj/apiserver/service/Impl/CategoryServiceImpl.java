package com.shj.apiserver.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shj.apiserver.entity.Category;
import com.shj.apiserver.mapper.CategoryMapper;
import com.shj.apiserver.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
