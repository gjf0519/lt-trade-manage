package com.lt.task;

import com.alibaba.fastjson.JSON;
import com.lt.entity.ClinchDetail;
import com.lt.entity.FundEntity;
import com.lt.service.FundService;
import com.lt.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author gaijf
 * @description
 * @date 2020/2/25
 */
@Slf4j
public class ClinchExtract {
    @Autowired
    FundService fundService;
    @Autowired
    private RestTemplate restTemplate;
    private CountDownLatch latch = null;
    @Autowired
    ThreadPoolExecutor threadPoolExecutor;

    @Scheduled(cron = "0 0 20 * * ?")
    public void execute(){
        List<List<String>> listCodes = RealCodeUtil.getCodesList(400, Constants.STOCK_CODE,null);
        latch = new CountDownLatch(listCodes.size());
        String date = TimeUtil.dateFormat(new Date(),"yyyyMMdd");
//        String date = "20200227";
        for (int i = 0; i < listCodes.size(); i++) {
            threadPoolExecutor.execute(
                    new DownLoadThread(listCodes.get(i), restTemplate,latch,getPath(date),getUrl(date)));
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //解析交易明细数据
        this.parseExcel(date);
    }

    public static String getPath(String date){
        return "E:\\excel\\stock\\capital\\"+date+"\\";
    }

    public static String getUrl(String date){
        //http://quotes.money.163.com/cjmx/2020/20200225/1002662.xls
        return "http://quotes.money.163.com/cjmx/"+ LocalDate.now().getYear() +"/"+date+"/";
    }

    private void parseExcel(String date){
        String directory = getPath(date);
        List<String> fileNames = FileUtil.getAllFileName(directory);
        if(fileNames == null || fileNames.size() == 0){
            log.info("没有下载文件");
            return;
        }
        for(String fileName:fileNames){
            //解析下载的excel文件
            List<ClinchDetail> list = FileUtil.readExcel(ClinchDetail.class,directory,fileName);
            //转换为CapitalInfo对象
            String code = fileName.substring(0,7);
            FundEntity fundEntity = this.transformCapital(list,code);
            if (fundEntity != null){
                try {
                    fundService.updRepetitive(fundEntity);
                }catch (Exception e){
                    log.info("交易明细数据解析异常:{} Exception:{}", JSON.toJSONString(fundEntity),e);
                }
            }
        }
    }

    private FundEntity transformCapital(List<ClinchDetail> list, String code){
        if (null == list || list.isEmpty()){
            log.info("没有交易明细记录:{}",code);
            return null;
        }
        //按每分钟对数据进行分组
        Map<Date,List<ClinchDetail>> timeGroup = list.stream().collect(Collectors.groupingBy(ClinchDetail::getClinchTime));
        //计算每分钟相同成交量占比
        Iterator<Map.Entry<Date, List<ClinchDetail>>> it = timeGroup.entrySet().iterator();
        //每分钟重复数据占比
        Map<Date,Double> repetitionMap = new HashMap<>(timeGroup.size());
        int repetitiveAllNum = 0;
        while(it.hasNext()) {
            Map.Entry<Date, List<ClinchDetail>> entry=it.next();
            List<ClinchDetail> items = entry.getValue();
            //按照成交手数分组
            Map<Integer,List<ClinchDetail>> quantityGroup = items.stream().collect(Collectors.groupingBy(ClinchDetail::getClinchQuantity));
            List<Integer> sortList = new ArrayList<>();
            for(List<ClinchDetail> value : quantityGroup.values()){
                sortList.add(value.size());
            }
            Collections.sort(sortList);
            int repetitiveNum = sortList.get(sortList.size()-1);
            if (sortList.get(sortList.size()-1) > 1){
                repetitiveAllNum = repetitiveAllNum+repetitiveNum;
            }
            double repetition = BigDecimalUtil.div(sortList.get(sortList.size()-1),items.size(),2);
            repetitionMap.put(entry.getKey(),repetition);
        }
        double allRepetition = BigDecimalUtil.div(repetitiveAllNum,list.size(),4);
        code = code.startsWith("0")?code.replaceFirst("0","sh"):
                code.replaceFirst("1","sz");
        return FundEntity.builder()
                .stockCode(code)
//                .createTime("2020-02-27")
                .createTime(LocalDate.now().toString())
                .redoMinutePct(JSON.toJSONString(repetitionMap))
                .redoAllPct(allRepetition)
                .build();
    }
}
