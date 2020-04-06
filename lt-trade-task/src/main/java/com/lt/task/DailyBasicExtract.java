package com.lt.task;

import com.lt.service.TushareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author gaijf
 * @description 每日指标
 * @date 2020/2/26
 */
@Slf4j
public class DailyBasicExtract {
    @Autowired
    TushareService tushareService;

    @Scheduled(cron = "0 0 19 * * ? ")// 0/1 * * * * *
    public void execute() {
        tushareService.transactionDetail();
    }
}
