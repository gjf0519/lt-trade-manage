package com.lt.utils;

import com.lt.entity.TushareResult;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author gaijf
 * @description
 * @date 2020/2/25
 */
@Slf4j
public class TushareUtil {

    public static final String URL = "http://api.waditu.com";
    public static final String TUSHARE_TOKEN = "79d2b64fa07ce8f0fe6009ae8f25e5b4fd3cdcf78cf785eec3b5ab12";

    public static List<Map<String,Object>> resultBuild(TushareResult tushareResult){
        List<String> fields = tushareResult.getData().getFields();
        List<List<String>> items = tushareResult.getData().getItems();
        List<Map<String,Object>> result = new ArrayList<>();
        items.stream().forEach(item -> {
            Map<String,Object> map = new HashMap<>();
            Stream.iterate(0, i -> i+1).limit(item.size())
                    .forEach(i -> {
                        map.put(fields.get(i),item.get(i));
                    });
            result.add(map);
        });
        List<Map<String, Object>> finalResult = result.stream()
                .filter(o -> !o.get("ts_code").toString().startsWith("3"))
                .collect(Collectors.toList());
        List<String> codes = new ArrayList<>(finalResult.size());
        finalResult.forEach(o ->{
            String code = o.get("ts_code").toString();
            String [] itemAray = code.split("\\.");
            code = itemAray[1].toLowerCase()+itemAray[0];
            o.put("ts_code",code);
            codes.add(code);
        });
        return finalResult;
    }
}
