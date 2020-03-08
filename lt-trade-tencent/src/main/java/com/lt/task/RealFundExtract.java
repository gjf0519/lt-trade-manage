package com.lt.task;

import com.lt.entity.FundEntity;
import com.lt.service.FundService;
import com.lt.utils.Constants;
import com.lt.utils.RealCodeUtil;
import com.lt.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author gaijf
 * @description 实时资金流向定时任务
 * @date 2020/2/24
 */
@Slf4j
public class RealFundExtract {
    private static final String url = "http://qt.gtimg.cn/q=";
    private static final int splitSize = 400;
    private static final String prefix = "ff_";
    private static final String startAm = "09:30:00";
    private static final String endAm = "11:30:00";
    private static final String startPm = "12:59:59";
    private static final String endPm = "24:00:00";
    private static final String timeFormat = "HH:mm:ss";
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    FundService fundService;
    @Autowired
    ThreadPoolExecutor threadPoolExecutor;

    @Scheduled(cron = "0 45 18 * * ? ")// 0/1 * * * * *
    public void execute() throws ParseException {
        List<String> codes = RealCodeUtil.getCodesStr(splitSize,Constants.STOCK_CODE,prefix);
        if (TimeUtil.isEffectiveDate(startAm,endAm,timeFormat)
                || TimeUtil.isEffectiveDate(startPm,endPm,timeFormat)){
            for (int i = 0; i < codes.size(); i++) {
                System.out.println(codes.get(i));
                threadPoolExecutor.execute(new RealThread(codes.get(i)));
            }
            long count = threadPoolExecutor.getTaskCount()-threadPoolExecutor.getCompletedTaskCount();
            System.out.println("RealFundExtract:总数:"+threadPoolExecutor.getTaskCount()+"完成:"+
                    threadPoolExecutor.getCompletedTaskCount()+"等待:"+count+"线程数量:"+threadPoolExecutor.getPoolSize());
        }
    }

    private class RealThread implements Runnable {
        private String codes;
        public RealThread(String codes){
            this.codes = codes;
        }
        @Override
        public void run() {
            try {
                ResponseEntity<String> entity = restTemplate.getForEntity(url+codes,String.class);
                if (null == entity || entity.getBody() == null){
                    return;
                }
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
                    FundEntity fundEntity = FundEntity.builder()
                            .stockCode(code)
                            .makersFundIn(Double.valueOf(values[1]))
                            .makersFundOut(Double.valueOf(values[2]))
                            .makersInFlow(Double.valueOf(values[3]))
                            .retailFuntIn(Double.valueOf(values[5]))
                            .retailFuntOut(Double.valueOf(values[6]))
                            .retailInFlow(Double.valueOf(values[7]))
                            .amounts(Double.valueOf(values[9]))
                            .createTime(LocalDate.now().toString())
                            .build();
                    fundService.saveFund(fundEntity);
                }
            } catch (Exception e){
                log.info("实时资金数据获取异常",e);
            }
        }
    }
}
