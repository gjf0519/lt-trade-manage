package com.lt.task;

import com.alibaba.fastjson.JSON;
import com.lt.entity.ClinchDetail;
import com.lt.entity.FundEntity;
import com.lt.service.FundService;
import com.lt.utils.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
 * @description 交易明细
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
//        List<List<String>> listCodes = RealCodeUtil.getCodesList(400, Constants.STOCK_CODE,null);
//        latch = new CountDownLatch(listCodes.size());
//        String date = TimeUtil.dateFormat(new Date(),"yyyyMMdd");
        String date = "20200403";
//        for (int i = 0; i < listCodes.size(); i++) {
//            threadPoolExecutor.execute(
//                    new DownLoadThread(listCodes.get(i), restTemplate,latch,getPath(date),getUrl(date)));
//        }
//        try {
//            latch.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //解析交易明细数据
        this.parseExcel(date);
    }

    public static String getPath(String date){
//        return "/home/stock/capital/"+date+"/";
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
//                    fundService.saveRepetitive(fundEntity);
                    fundService.updateClinchChangeMinute(fundEntity);
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
//        Map<String,Double> redoMinuteMap = new HashMap<>();
        //去重分钟交易手数
        Set<Integer> handMinute = null;
        //上午重复数据占比
        Set<Integer> handAm = new HashSet<>();
        int handAllAm = 0;
        //去重当天交易手数
        Set<Integer> handAll = new HashSet<>();
        Date date = TimeUtil.StringToDate("11:30","HH:mm");

        List<RealMinute> minutes = new ArrayList<>();
        RealMinute realMinute = null;
        //每分钟重复数据占比
        for(Map.Entry<Date,List<ClinchDetail>> entry : timeGroup.entrySet()){
            realMinute = new RealMinute();
            handMinute = new HashSet<>();
            handMinute = new HashSet<>();
            List<ClinchDetail> vars = entry.getValue();
            double clinchChangeMinute = 0;
            for(ClinchDetail detail : vars){
                if(detail.getClinchTime().before(date)){
                    handAllAm++;
                    handAm.add(detail.getClinchQuantity());
                }
                handMinute.add(detail.getClinchQuantity());
                handAll.add(detail.getClinchQuantity());
                clinchChangeMinute = BigDecimalUtil.add(clinchChangeMinute,detail.getClinchChange(),2);
            }
            //计算每分钟重复手数占比
//            double repetition = BigDecimalUtil.div(handMinute.size(),vars.size(),2);
//            repetition = 1 - repetition;
//            redoMinuteMap.put(TimeUtil.dateFormat(entry.getKey(),"HH:mm"),repetition);
            realMinute.setClinchChangeMinute(clinchChangeMinute);
            realMinute.setReaTime(TimeUtil.dateFormat(entry.getKey(),"HH:mm"));
            minutes.add(realMinute);
        }
        //计算上午重复手数占比
        double repetitionAm = BigDecimalUtil.div(handAm.size(),handAllAm,2);
        repetitionAm = 1 - repetitionAm;
        //计算当天重复手数占比
        double repetitionAll = BigDecimalUtil.div(handAll.size(),list.size(),2);
        repetitionAll = 1 - repetitionAll;

        code = code.startsWith("0")?code.replaceFirst("0","sh"):
                code.replaceFirst("1","sz");
        return FundEntity.builder()
                .stockCode(code)
                .createTime("2020-04-03")
//                .createTime(LocalDate.now().toString())
//                .redoMinutePct(JSON.toJSONString(redoMinuteMap))
                .clinchChangeMinute(JSON.toJSONString(minutes))
                .repetitionAmPct(repetitionAm)
                .redoAllPct(repetitionAll)
                .dealNum(handAllAm)
                .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RealMinute {
        private String reaTime;
        private double clinchChangeMinute;
    }

}
