package com.lt.task;

import com.alibaba.fastjson.JSON;
import com.lt.utils.Constants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * @author gaijf
 * @description
 * @date 2020/4/15
 */
@SpringBootTest
public class Fund163 {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Test
    public void fund(){
        List<String> dailyBasics = redisTemplate.opsForList().range("real-fund-163-600758", 0, -1);
        System.out.println(JSON.toJSONString(dailyBasics));
    }
}
