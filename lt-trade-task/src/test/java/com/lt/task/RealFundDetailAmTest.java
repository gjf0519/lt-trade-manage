package com.lt.task;

import com.lt.utils.RestTemplateUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author gaijf
 * @description
 * @date 2020/4/9
 */
@SpringBootTest
public class RealFundDetailAmTest {
    @Autowired
    RealFundDetailAm realFundDetailAm;

    @Test
    public void execute() {
        realFundDetailAm.execute();
    }

    public static void main(String[] args) {
        for (int i = 0; ; i++) {
            String result = RestTemplateUtil.get("http://quotes.money.163.com/service/zhubi_ajax.html?symbol=600758&end=10:00:00", null);
            System.out.println(result);
            if (StringUtils.isBlank(result)) {
                break;
            }
        }
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

}
