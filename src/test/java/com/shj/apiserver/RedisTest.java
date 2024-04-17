package com.shj.apiserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisTest {

    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    @Resource
    private ListOperations<String, Integer> ops;

    @Test
    void name() {
//        ListOperations<String, Integer> ops = redisTemplate.opsForList();
        Stack<Integer> integers = new Stack<>();
        ops.leftPush("his",68);
        ops.leftPush("his",34);
        ops.leftPush("his",123);
        ops.leftPush("his",4);
        List<Integer> range = ops.range("his", 0, -1);
        System.out.println(range);
    }

    @Test
    void randomPush() {
        for (int i = 0; i < 20; i++) {
            ops.rightPush("his", new Random().nextInt(10000)+1);
        }
        // 设置 "his" 列表的过期时间为 1 小时（以秒为单位）
        redisTemplate.expire("his", 1, TimeUnit.HOURS);
        List<Integer> range = ops.range("his", 0, -1);
        System.out.println(range);
    }


    @Test
    void previewRedis() {
        List<Integer> range = ops.range("his", 0, -1);
        System.out.println(range);
    }
}
