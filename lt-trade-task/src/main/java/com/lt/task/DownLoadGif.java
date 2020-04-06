package com.lt.task;

import com.lt.utils.Constants;
import com.lt.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

/**
 * @author gaijf
 * @description 现在分时K线图
 * @date 2020/3/31
 */
public class DownLoadGif {
    @Autowired
    private RestTemplate restTemplate;

    @Scheduled(cron = "0 0 12 * * ? ")
    public void execute() {
        String date = TimeUtil.dateFormat(new Date(),"yyyyMMdd");
        String path = "/home/stock/K线图/"+date+"/";
        for(String code : Constants.STOCK_CODE){
            DownLoadThread.downloadHttp(restTemplate, "http://image.sinajs.cn/newchart/min/n/"+code+".gif", path+code+".gif", 0);
        }
    }
}
