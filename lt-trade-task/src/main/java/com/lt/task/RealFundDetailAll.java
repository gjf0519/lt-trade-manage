package com.lt.task;

import com.alibaba.fastjson.JSON;
import com.lt.entity.ClinchDetail;
import com.lt.entity.FundEntity;
import com.lt.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author gaijf
 * @description 当天交易明细数据获取
 * @date 2020/4/1
 */
@Slf4j
public class RealFundDetailAll {
    @Autowired
    ThreadPoolExecutor threadPoolExecutor;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Scheduled(cron = "0 0 16 * * ? ")// 0/1 * * * * *
    public void execute() {
        List<String> codes = Constants.STOCK_CODE;
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        List<ClinchDetail> list = null;
        for(String code : codes){
            list = new ArrayList<>();
            for(int i = 0;;i++){
                String result = RestTemplateUtil.get("http://stock.gtimg.cn/data/index.php?appn=detail&action=data&c="+code+"&p="+i+"",null);
                if(StringUtils.isBlank(result)){
                    break;
                }
                String [] details = result.split("=");
                String fundsStr = details[1];
                fundsStr = fundsStr.substring(3,fundsStr.length()-2);
                String [] fundsArray = fundsStr.split("\\|");
                ClinchDetail detail = null;
                for(String str : fundsArray){
                    detail = new ClinchDetail();
                    String [] fund = str.split("/");
                    detail.setClinchTime(TimeUtil.StringToDate(fund[1],formatter));
                    detail.setClinchPrice(Double.valueOf(fund[2]));
                    detail.setClinchChange(Double.valueOf(fund[3]));
                    detail.setClinchQuantity(Integer.valueOf(fund[4]));
                    detail.setClinchSum(Double.valueOf(fund[5]));
//                    detail.setClinchNature(fund[6]);
                    list.add(detail);
                }
            }
            if(list.isEmpty()){
                continue;
            }
            FundEntity fundEntity = transformAllCapital(list,code);
            FileWriteUtil.writeTXT(FileWriteUtil.getTextPath(),"lt_fund_detail_all",JSON.toJSONString(fundEntity));
            try{
                redisTemplate.opsForList().rightPushAll("lt_fund_detail_all",JSON.toJSONString(fundEntity));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        log.info("===============当天交易明细数据获取任务结束===========");
    }

    class RealFundThread implements Runnable{
        private List<String> codes;
        public RealFundThread(List<String> codes){
            this.codes = codes;
        }
        @Override
        public void run() {

        }
    }

    private FundEntity transformAllCapital(List<ClinchDetail> list, String code){
        if (null == list || list.isEmpty()){
            log.info("没有交易明细记录:{}",code);
            return null;
        }
        //去重当天交易手数
        Set<Integer> handAll = new HashSet<>();
        for(ClinchDetail item : list){
            handAll.add(item.getClinchQuantity());
        }
        //计算当天重复手数占比
        double repetitionAll = BigDecimalUtil.div(handAll.size(),list.size(),2);
        repetitionAll = 1 - repetitionAll;

        return FundEntity.builder()
                .stockCode(code)
                .createTime(LocalDate.now().toString())
                .redoAllPct(repetitionAll)
                .build();
    }
}
