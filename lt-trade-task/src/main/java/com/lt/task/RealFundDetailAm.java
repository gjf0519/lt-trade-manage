package com.lt.task;

import com.alibaba.fastjson.JSON;
import com.lt.entity.ClinchDetail;
import com.lt.entity.FundEntity;
import com.lt.entity.FundReal;
import com.lt.utils.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author gaijf
 * @description 实时交易明细数据获取
 * @date 2020/4/1
 */
@Slf4j
public class RealFundDetailAm {

    private static final String key = "lt_fund_detail_am";
    private static final int splitSize = 400;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    ThreadPoolExecutor threadPoolExecutor;
    private static List<FundReal> fundReals = null;

    @Scheduled(cron = "0 50 11 * * ? ")// 0/1 * * * * *
    public void execute() {
        fundReals = new ArrayList<>(Constants.STOCK_CODE.size());
        List<String> fundRealAms = redisTemplate.opsForList().range("lt_fund_real", 0, -1);
        for (String fund : fundRealAms) {
            fundReals.add(JSON.parseObject(fund, FundReal.class));
        }
        List<List<String>> codes = RealCodeUtil.getCodesList(splitSize,Constants.STOCK_CODE,null);
        CountDownLatch latch = new CountDownLatch(codes.size());
        for (int i = 0; i < codes.size(); i++) {
            threadPoolExecutor.execute(new FundDetailThread(key,0,codes.get(i),latch,redisTemplate));
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            log.info("=============================上午交易明细指标收集异常========================");
            e.printStackTrace();
        }
        redisTemplate.expire(key, 20 * 60 * 60, TimeUnit.SECONDS);
        log.info("=============================上午交易明细指标收集完成========================");
    }

    public static List<FundReal> getFundReals(){
        return fundReals;
    }
}
