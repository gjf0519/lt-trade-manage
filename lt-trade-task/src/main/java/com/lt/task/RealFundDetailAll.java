package com.lt.task;

import com.alibaba.fastjson.JSON;
import com.lt.entity.ClinchDetail;
import com.lt.entity.FundEntity;
import com.lt.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author gaijf
 * @description 当天交易明细数据获取
 * @date 2020/4/1
 */
@Slf4j
public class RealFundDetailAll {
    private static final String key = "lt_fund_detail_all";
    private static final int splitSize = 400;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    ThreadPoolExecutor threadPoolExecutor;

    @Scheduled(cron = "0 0 16 * * ? ")// 0/1 * * * * *
    public void execute() {
        List<List<String>> codes = RealCodeUtil.getCodesList(splitSize,Constants.STOCK_CODE,null);
        CountDownLatch latch = new CountDownLatch(codes.size());
        for (int i = 0; i < codes.size(); i++) {
            threadPoolExecutor.execute(new FundDetailThread(key,1,codes.get(i),latch,redisTemplate));
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            log.info("=============================当天交易明细指标收集异常========================");
            e.printStackTrace();
        }
        redisTemplate.expire(key, 20 * 60 * 60, TimeUnit.SECONDS);
        log.info("=============================当天交易明细指标收集完成========================");
    }
}
