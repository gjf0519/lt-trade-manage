package com.lt.task;

import com.lt.entity.FundReal;
import com.lt.utils.Constants;
import com.lt.utils.RealCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author gaijf
 * @description 实时成交信息
 * @date 2020/2/24
 */
@Slf4j
public class RealFundExtract {
    private static final String url = "http://qt.gtimg.cn/q=";
    private static final int splitSize = 400;
    private static final String prefix = "ff_";
    private static final String topic = "REAL-STOCK-INFO";
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ThreadPoolTaskExecutor threadPoolExecutor;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Scheduled(cron = "0 35 11 * * ? ")// 0/1 * * * * *
    public void execute() {
        List<String> codes = RealCodeUtil.getCodesStr(splitSize,Constants.STOCK_CODE,prefix);
        CountDownLatch latch = new CountDownLatch(codes.size());
        for (int i = 0; i < codes.size(); i++) {
            threadPoolExecutor.execute(new RealThread(codes.get(i),latch));
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            log.info("=============================实时资金流向定时任务收集异常========================");
            e.printStackTrace();
        }
        log.info("=============================实时资金流向定时任务收集完成========================");
    }

    private class RealThread implements Runnable {
        private String codes;
        private CountDownLatch latch;
        public RealThread(String codes,CountDownLatch latch){
            this.codes = codes;
            this.latch = latch;
        }
        @Override
        public void run() {
            try {
                ResponseEntity<String> entity = restTemplate.getForEntity(url+codes,String.class);
                if (null == entity || entity.getBody() == null){
                    return;
                }
                codes = codes.replaceAll(prefix,"");
                ResponseEntity<String> change = restTemplate.getForEntity("http://qt.gtimg.cn/q="+codes,String.class);
                //实时交易信息
                Map<String,Double[]> pctChgs = resultSplit(change.getBody());
                //实时资金流向
                StringTokenizer token = new StringTokenizer(entity.getBody(),";");
                while(token.hasMoreTokens()){
                    String result = token.nextToken();
                    if (StringUtils.isEmpty(result.trim()))
                        continue;
                    String[] values = result.split("~");
                    if(values.length < 9)
                        continue;
                    String code = values[0].split("=")[1];
                    code = code.substring(1,code.length());
                    String pctCode = code.substring(2,code.length());
                    Double [] realMarket = pctChgs.get(pctCode);
                    double amount = Double.valueOf(values[9]);
                    FundReal fundEntity = FundReal.builder()
                            .stockCode(code)
                            .openPrice(realMarket[0])
                            .makersFundIn(Double.valueOf(values[1]))
                            .makersFundOut(Double.valueOf(values[2]))
                            .makersInFlow(Double.valueOf(values[3]))
                            .retailFuntIn(Double.valueOf(values[5]))
                            .retailFuntOut(Double.valueOf(values[6]))
                            .retailInFlow(Double.valueOf(values[7]))
                            .amounts(amount)
                            .pctChg(realMarket[1])
                            .exchange(realMarket[2])
                            .createTime(LocalDate.now().toString())
                            .build();
                    rocketMQTemplate.convertAndSend(topic, fundEntity);
                }
            } catch (Exception e){
                log.info("实时资金数据获取异常",e);
            }
            latch.countDown();
        }

        /**
         * 按照特定的规则拆分数据
         * @return
         */
        public Map<String,Double[]> resultSplit(String results){
            Map<String,Double[]> map = new HashMap<>();
            StringTokenizer token = new StringTokenizer(results,";");
            while(token.hasMoreTokens()){
                String result = token.nextToken();
                if (StringUtils.isEmpty(result.trim()))
                    continue;
                String[] values = result.split("~");
                if(values.length < 38)
                    continue;
                Double [] var = new Double[3];
                var[0] = Double.valueOf(values[5]);//开盘价
                var[1] = Double.valueOf(values[32]);//涨幅
                var[2] = Double.valueOf(values[38]);//换手率
                map.put(values[2],var);
            }
            return map;
        }
    }
}
