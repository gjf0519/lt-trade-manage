package com.lt.task;

import com.lt.entity.RealDeal;
import com.lt.utils.Constants;
import com.lt.utils.RestTemplateUtil;
import com.lt.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

/**
 * @author gaijf
 * @description 网易财经交易明细数据
 * @date 2020/4/14
 */
@Slf4j
public class RealDealRecord {
    private static final int splitSize = 400;
    private static final String startAm = "09:30:00";
    private static final String endAm = "11:30:00";
    private static final String timeFormat = "HH:mm:ss";
    private static final String topic = "WY-DEAL-RECORD";
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ThreadPoolTaskExecutor threadPoolExecutor;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Scheduled(cron = "0 */5 * * * ?")
    public void execute(){
//        if (TimeUtil.isEffectiveDate(startAm,endAm,timeFormat)){
            String time = TimeUtil.dateFormat(new Date(),"HH:mm");
            time = "14:00";
            List<String> codes = Constants.STOCK_CODE;
            for (int i = 0; i < codes.size(); i++) {
                threadPoolExecutor.execute(new RealDealRecord.RealThread(time,codes.get(i)));
            }
//        }
    }

    private class RealThread implements Runnable {
        private String time;
        private String code;
        public RealThread(String time,String code){
            this.time = time;
            this.code = code;
        }
        @Override
        public void run() {
            String subCode = code.substring(2,code.length());
            String record = RestTemplateUtil.get("http://quotes.money.163.com/service/zhubi_ajax.html?symbol="+subCode+"&end="+time+":00", null);
            RealDeal item = RealDeal.builder()
                    .code(code)
                    .recordStrs(record)
                    .build();
            // 发送消息
            rocketMQTemplate.convertAndSend(topic, item);
        }
    }
}
