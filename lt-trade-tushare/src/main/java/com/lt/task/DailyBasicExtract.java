package com.lt.task;

import com.lt.sevice.TushareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.ParseException;

/**
 * @author gaijf
 * @description
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
