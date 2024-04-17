package com.shj.apiserver.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shj.apiserver.entity.ApiResponse;
import com.shj.apiserver.entity.Category;
import com.shj.apiserver.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.shj.apiserver.entity.ResStatus.NO_CHILD_ID;
import static com.shj.apiserver.entity.ResStatus.NO_SUCH_TYPE;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 获取分类列表,
     * type: all 获取全部分类
     * parent 获取父级分类
     * children 子级分类
     *
     * @param type
     * @return
     */
    @GetMapping("/get")
    public Object getCategories(@RequestParam("type") String type, Integer pid) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        if (type.equals("all")) {
            return ApiResponse.success(categoryService.list(), "返回所有分类");
        }
        if (type.equals("parent")) {
            return ApiResponse.success(categoryService.list(wrapper.eq(Category::getParentid, 0)), "返回所有父级分类");
        }
        if (type.equals("children")) {
            if (pid == null) {
                return ApiResponse.error(NO_CHILD_ID, "未传递子分类");
            }
            return ApiResponse.success(categoryService.list(wrapper.eq(Category::getParentid, pid)), String.format("返回%s对应的所有子级分类", pid));
        }
        return ApiResponse.error(NO_SUCH_TYPE, "没有这样的type");
    }

}
