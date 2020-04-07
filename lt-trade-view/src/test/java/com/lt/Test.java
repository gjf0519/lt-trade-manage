package com.lt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author gaijf
 * @description
 * @date 2020/3/25
 */
public class Test {

    public static void main(String[] args) {
//        List<String> names = new ArrayList<>();
//        String str = "{company_name=Dr. Gerhard Mann, Chem.-Pharm. Fabrik GmbH}, {company_name=山东博士伦福瑞达制药有限公司}";
//        String regex = "\\{(.*?)}";
//
//        Pattern p = Pattern.compile(regex);
//        Matcher m = p.matcher(str);
//        while (m.find()) {
//            String g = m.group();
//            String target = g.substring(1, g.length() - 1);//去掉花括号
//            System.out.println(target);
//        }

//将结果转换为float
        double numberC = 6;
//可以取到小数点后的正确数值，如果两个都是整形，那小数点后面就会清零
        System.out.println(numberC);
        double number = numberC / 5;
        System.out.println(number);
        int renewNum = (int)Math.ceil(number);
        System.out.println(renewNum);
    }

    static class Mm{
        private String gg;
        private String ff;

        public String getGg() {
            return gg;
        }

        public void setGg(String gg) {
            this.gg = gg;
        }

        public String getFf() {
            return ff;
        }

        public void setFf(String ff) {
            this.ff = ff;
        }
    }
}
