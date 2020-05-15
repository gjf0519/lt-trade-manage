package com.lt.task;

import com.lt.entity.ClinchDetail;
import com.lt.utils.Constants;
import com.lt.utils.RestTemplateUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author gaijf
 * @description
 * @date 2020/4/9
 */
@SpringBootTest
public class RealFundDetailAmTest {
    @Autowired
    RealDealRecord realFundDetailAm;

    @Test
    public void execute() throws ParseException {
        realFundDetailAm.execute();
    }

    public static void main(String[] args) {
        getFundsTx();
//        for (int i = 0; ; i++) {
//            //http://quotes.money.163.com/service/zhubi_ajax.html?symbol=000050&end=14%3A37%3A46
//            String result = RestTemplateUtil.get("http://quotes.money.163.com/service/zhubi_ajax.html?symbol=600758&end=10:00:00", null);
//            System.out.println(result);
//            if (StringUtils.isBlank(result)) {
//                break;
//            }
//        }
    }
    public void getFunds(){
        for (int i = 0; ; i++) {
//            String result = RestTemplateUtil.get("http://stock.gtimg.cn/data/index.php?appn=detail&action=data&c=sh600758&p=" + i + "", null);
            String result = RestTemplateUtil.get("http://quotes.money.163.com/service/zhubi_ajax.html?symbol=600758&end=11%3A31%3A25", null);
            System.out.println(result);
            if (StringUtils.isBlank(result)) {
                break;
            }
        }
    }

    public static void getFundsTx(){
        List<String> codes = Constants.STOCK_CODE;
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        List<ClinchDetail> list = null;
        for(int i = 0;;i++){
            String result = RestTemplateUtil.get("http://stock.gtimg.cn/data/index.php?appn=detail&action=data&c="+"sh600527"+"&p="+i+"",null);
            System.out.println(result);
            if(StringUtils.isBlank(result)){
                break;
            }
        }
    }

}
