package com.shj.apiserver.controller;

import ch.qos.logback.core.joran.conditional.ElseAction;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shj.apiserver.entity.Category;
import com.shj.apiserver.entity.Thread;
import com.shj.apiserver.service.CategoryService;
import com.shj.apiserver.service.ThreadService;
import com.shj.apiserver.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/thread")
public class ThreadController {

    @Resource
    private ThreadService threadService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private OkHttpClient client;

    /**
     * 获取分类的文章
     *
     * @param pid    分类标识
     * @param page   页数
     * @param size   尺寸
     * @param source 从哪里获取, 1 网络, 0 数据库
     * @return
     * @throws IOException
     */
    @GetMapping("/get")
    public Object getThreads(@RequestParam("pid") int pid, @RequestParam(value = "page", required = false) Integer page,
                             @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "source", required = false) Integer source) throws IOException {
        if (page == null || size == null) {
            page = 1;
            size = 10;
        }
        if (source == null) {
            source = 1;
        }
        // 从网络中获取
        if (source == 1) {
            String formatUrl = String.format("https://www.arxivdaily.com/api/v3/thread.list?perPage=%s&page=%s&filter[categoryids][0]=%s", size, page, pid);
            log.info("本次请求: {}", formatUrl);
            Response response = client.newCall(new Request.Builder()
                    .url(formatUrl)
                    .get()
                    .build()).execute();
            try {
                return JsonUtils.Json2Arxiv(response.body().string());
            } finally {
                response.close();
            }
        }
        // 从本地中获取
        IPage<Thread> iPage = new Page<>(page, size);
        if (source == 0) {
            LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
            Category one = categoryService.getOne(categoryLambdaQueryWrapper.eq(Category::getPid, pid));

            LambdaQueryWrapper<Thread> threadLambdaQueryWrapper = new LambdaQueryWrapper<>();
            return threadService.page(iPage, threadLambdaQueryWrapper.eq(Thread::getCategoryId, one.getCategoryId()));
        }
        return null;
    }

}
