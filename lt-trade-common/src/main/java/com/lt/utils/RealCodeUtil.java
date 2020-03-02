package com.lt.utils;

import com.sun.xml.internal.ws.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RealCodeUtil {

    public static List<String> getCodesStr(int splitSize, List<String> codes,String prefix){
        List<String> list = new ArrayList<>();
        // 总数据条数
        int dataSize = codes.size();
        // 线程数
        int paragraphSize = dataSize / splitSize + 1;
        // 定义标记,过滤threadNum为整数
        boolean special = dataSize % splitSize == 0;
        for (int i = 0; i < paragraphSize; i++) {
            String result = null;
            List<String> items = null;
            if (i == paragraphSize - 1) {
                if (special)
                    break;
                items = codes.subList(splitSize * i, dataSize);
            } else {
                items = codes.subList(splitSize * i, splitSize * (i + 1));
            }
            joinPrefix(items,prefix);
            result = String.join(",",items);
            list.add(result);
        }
        return list;
    }

    public static List<List<String>> getCodesList(int splitSize,List<String> codes,String prefix){
        List<List<String>> list = new ArrayList<>();;
        // 总数据条数
        int dataSize = codes.size();
        // 线程数
        int paragraphSize = dataSize / splitSize + 1;
        // 定义标记,过滤threadNum为整数
        boolean special = dataSize % splitSize == 0;
        for (int i = 0; i < paragraphSize; i++) {
            List<String> items = null;
            if (i == paragraphSize - 1) {
                if (special)
                    break;
                items = codes.subList(splitSize * i, dataSize);
            } else {
                items = codes.subList(splitSize * i, splitSize * (i + 1));
            }
            joinPrefix(items,prefix);
            list.add(items);
        }
        return list;
    }

    public static void joinPrefix(List<String> codes,String prefix){
        if(null != prefix && !"".endsWith(prefix)){
            for (int i = 0;i < codes.size();i++){
                String code = codes.get(i);
                code = prefix+code;
                codes.set(i,code);
            }
        }
    }
}
