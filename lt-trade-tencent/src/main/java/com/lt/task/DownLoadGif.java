package com.lt.task;

import com.lt.utils.Constants;
import com.lt.utils.RealCodeUtil;
import com.lt.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @author gaijf
 * @description 现在分时K线图
 * @date 2020/3/31
 */
@Component
public class DownLoadGif {
    @Autowired
    private RestTemplate restTemplate;

    public void execute() {
//        List<List<String>> codes = RealCodeUtil.getCodesList(400, Constants.STOCK_CODE,null);
        String date = TimeUtil.dateFormat(new Date(),"yyyyMMdd");
        String path = "E:\\excel\\stock\\K线图\\"+date+"\\";
        for(String code : Constants.STOCK_CODE){
            DownLoadThread.downloadHttp(restTemplate, "http://image.sinajs.cn/newchart/min/n/"+code+".gif", path+code+".gif", 0);
        }
    }
}
