package com.lt.task;

import com.lt.sevice.TushareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author gaijf
 * @description 日线行情
 * @date 2020/3/30
 */
@Slf4j
public class DailyExtract {
    @Autowired
    TushareService tushareService;

    public void execute() {
        tushareService.transactionDaily();
    }
}
