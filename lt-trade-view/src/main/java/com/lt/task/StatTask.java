package com.lt.task;

import com.lt.service.StatService;
import com.lt.service.TransformService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author gaijf
 * @description
 * @date 2020/5/15
 */
@Slf4j
public class StatTask {

    @Autowired
    StatService statService;

    @Scheduled(cron = "0 16 16 * * ? ")
    public void execute() {
        //持久化成交明细数据
        statService.saveDealRecord();
        //计算过去5日涨幅
        statService.calculateFivePctChg();
        //计算当天成交量与过去5日平均成交量占比
        statService.calculateFiveVolumeRatio();
        //清空计算时临时缓存数据
//        TransformService.clearCache();
    }
}
