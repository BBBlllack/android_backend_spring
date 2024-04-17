package com.shj.apiserver.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shj.apiserver.entity.ApiResponse;
import com.shj.apiserver.entity.ChatCompletionMessage;
import com.shj.apiserver.entity.Paper;
import com.shj.apiserver.entity.ResStatus;
import com.shj.apiserver.service.CategoryService;
import com.shj.apiserver.service.PaperService;
import com.shj.apiserver.service.ThreadService;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.shj.apiserver.entity.ResStatus.FLASK_URL;
import static com.shj.apiserver.entity.ResStatus.NO_SUCH_TYPE;

@Slf4j
@RestController
@RequestMapping("/paper")
public class PaperController {

    @Resource
    private PaperService paperService;

    @Resource
    private ThreadService threadService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private ListOperations<String, Integer> listOperations;

    @Resource
    private OkHttpClient client;

    private static ObjectMapper mapper;


    static {
        mapper = new ObjectMapper();
    }


    /**
     * @param type  可以取 latest random pid(此时需要传递一个pid)
     * @param pid   当type=pid时需要给，代表分类id
     * @param page  页码
     * @param size  数据条数
     * @param key   搜索关键字
     * @param favor 是否收藏, 默认为null
     * @return
     */
    @GetMapping("/get")
    public Object getPapers(@RequestParam("type") String type, Integer pid, Integer page, Integer size,
                            String key, Integer favor, Integer num) throws IOException {
        LambdaQueryWrapper<Paper> wrapper = new LambdaQueryWrapper<>();
        IPage<Paper> iPage;
        if (favor != null) {
            if (favor != 0 && favor != 1) {
                favor = null;
            }
        }
        if (favor != null) {
            wrapper.eq(Paper::getFavor, favor);
        }
        if (page == null || size == null) {
            page = 1;
            size = 10;
        }
        // 推荐算法, 根据文章推荐
        if (type.equals("rembyid")) {
            /**
             type = rembyid 此时传入pid作为文章id，num(1-50)为要推荐的数量
             */
            pid = pid == null ? 1 : pid;
            num = num == null ? 5 : num;
            listOperations.leftPush("his", pid);
            Request request = new Request.Builder()
                    .url(FLASK_URL + String.format("/recom/byid?id=%s&num=%s", pid, num))
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            List<Integer> ids = mapper.readValue(response.body().string(), List.class);
            return ApiResponse.success(paperService.listByIds(ids), String.format("返回文章%s的推荐%s篇文章", pid, num));
        }
        // 推荐算法, 根据访问记录推荐
        if (type.equals("rem")) {
            // 默认推荐10篇
            num = num == null ? 10 : num;
            int head = listOperations.leftPop("his");
            Request request = new Request.Builder()
                    .url(FLASK_URL + String.format("/recom/byid?id=%s&num=%s", head, num))
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            List<Integer> ids = mapper.readValue(response.body().string(), List.class);
            return ApiResponse.success(paperService.listByIds(ids), String.format("根据历史记录推荐", pid, num));
        }
        if (type.equals("latest")) {
            wrapper.orderByDesc(Paper::getCreatedAt);
            if (key != null) {
                wrapper.like(Paper::getTitleZh, key).or().like(Paper::getTitleEn, key);
            }
            iPage = new Page<>(page, size);
            return ApiResponse.success(paperService.page(iPage, wrapper), "返回最新的论文数据");
        }
        if (type.equals("random")) {
            // 随机获取数据
            page = new Random().nextInt(20) + 1;
            wrapper.orderByDesc(Paper::getCreatedAt);
            iPage = new Page<>(page, size);
            return ApiResponse.success(paperService.page(iPage, wrapper), "返回随机的论文数据");
        }
        if (type.equals("pid")) {
            if (pid == null) {
                return ApiResponse.error(400, "error pid is null!");
            }
            wrapper.orderByDesc(Paper::getCreatedAt);
            iPage = new Page<>(page, size);
            wrapper.eq(Paper::getPid, pid);
            if (key != null) {
                wrapper.like(Paper::getTitleZh, key).or().like(Paper::getTitleEn, key);
            }
            return ApiResponse.success(paperService.page(iPage, wrapper), "返回特定分类论文数据");
        }
        return ApiResponse.error(NO_SUCH_TYPE, "没有这样的type");
    }

    /**
     * @param type 可以取 rem(此时传num为推荐的数量，默认10) rembyid(此时传入id，为文章id，num默认为5)
     * @param pid
     * @param num
     * @return
     * @throws IOException
     */
    @GetMapping("/recom")
    @Cacheable(value = "rem", key = "#type + #pid + #num")
    public Object recom(@RequestParam("type") String type, @RequestParam(value = "id", required = false) Integer pid, Integer num) throws IOException {
        // 推荐算法, 根据文章推荐
        if (type.equals("rembyid")) {
            pid = pid == null ? 1 : pid;
            num = num == null ? 5 : num;
            listOperations.leftPush("his", pid);
            Request request = new Request.Builder()
                    .url(FLASK_URL + String.format("/recom/byid?id=%s&num=%s", pid, num))
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            List<Integer> ids = mapper.readValue(response.body().string(), List.class);
            return ApiResponse.success(paperService.listByIds(ids), String.format("返回文章%s的推荐%s篇文章", pid, num));
        }
        // 推荐算法, 根据访问记录推荐
        if (type.equals("rem")) {
            // 默认推荐10篇
            num = num == null ? 10 : num;
            int head = 0;
            try {
                head = listOperations.leftPop("his");
            } catch (Exception e) {
                head = new Random().nextInt(10000) + 1;
            }
            Request request = new Request.Builder()
                    .url(FLASK_URL + String.format("/recom/byid?id=%s&num=%s", head, num))
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            List<Integer> ids = mapper.readValue(response.body().string(), List.class);
            return ApiResponse.success(paperService.listByIds(ids), String.format("根据历史记录推荐", pid, num));
        }
        return ApiResponse.error(NO_SUCH_TYPE, "没有这样的type");
    }

    /**
     * 收藏和取消收藏
     *
     * @param pid
     * @return
     */
    @GetMapping("/favor")
    public Object favor(@RequestParam("id") Integer pid) {
        Paper paper = paperService.getById(pid);
        // 收藏了就取消，没有就收藏
        paper.setFavor(paper.getFavor() == 0 ? 1 : 0);
        return paperService.updateById(paper) ? ApiResponse.success(paper, "更新成功") : ApiResponse.error(500, "更新失败");
    }

    @GetMapping("/analy")
    @Cacheable(value = "analyCache", key = "#pids.hashCode()")
    public Object analy(@RequestParam("pids") int[] pids, HttpServletResponse servletResponse) throws IOException {
        Request request = new Request.Builder()
                .url(FLASK_URL + "/openai/analy?ids=" + Arrays.toString(pids))
                .get()
                .build();
        Response response = client.newCall(request).execute();
        try {
            servletResponse.setContentType("application/json;charset=utf-8");
            return response.body().string();
        } finally {
            response.close();
        }
    }

    @GetMapping("/summarize")
    @Cacheable(value = "summarizeCache", key = "#id")
    public Object summarize(@RequestParam("id") Integer id, HttpServletResponse servletResponse) throws IOException {
        Request request = new Request.Builder()
                .url(FLASK_URL + "/openai/summarize?id=" + id)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        try {
            servletResponse.setContentType("application/json;charset=utf-8");
            return response.body().string();
        } finally {
            response.close();
        }

    }
}
