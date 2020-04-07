package com.lt;

import com.alibaba.fastjson.JSON;
import com.lt.entity.FundReal;
import com.lt.service.TransformService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author gaijf
 * @description
 * @date 2020/4/7
 */
@SpringBootTest
public class TransformServiceTest {
    @Autowired
    TransformService transformService;

    @Test
    public void saveAmData(){
        transformService.saveAmData();
    }

    public void saveFundRealAm(){
        transformService.saveFundRealAm();
    }
}
