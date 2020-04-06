package com.lt.controller;

import com.lt.chart.RedoAllPctPillar;
import com.lt.common.exception.ResultEntity;
import com.lt.common.page.PageParams;
import com.lt.entity.FundEntity;
import com.lt.service.FundService;
import com.lt.vo.RedoAllPctVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gaijf
 * @description
 * @date 2020/3/24
 */
@Slf4j
@Controller
@RequestMapping("fund")
public class FundController {

    @Autowired
    FundService fundService;

    @RequestMapping("/list")
    public String toFundList(){
        return "trade/fund/fundList";
    }

    @RequestMapping("/listData")
    @ResponseBody
    public ResultEntity<List<FundEntity>> getFundList(@Param("page") int page,
                                                      @Param("limit") int limit){
        int totals = fundService.getFundCount();
        List<FundEntity> result = fundService.getFundList(PageParams.build(limit, page));
        return ResultEntity.success(result,totals);
    }

    @RequestMapping("/redoAllPctChart")
    @ResponseBody
    public ResultEntity<RedoAllPctPillar> redoAllPctChart(){
        List<RedoAllPctVo> redoAllPcts = fundService.getRedoAllPct();

        List<String> dimensions = new ArrayList<>();
        dimensions.add("product");
        dimensions.add("focus");
        dimensions.add("range");

        List<String> products = new ArrayList<>();
        products.add("0.0~0.05");
        products.add("0.05~0.1");
        products.add("0.1~0.15");
        products.add("0.15~0.2");
        products.add("0.2~0.25");
        products.add("0.25~0.3");
        products.add("0.3~0.35");
        products.add("0.35~0.4");
        products.add("0.4~0.45");
        products.add("0.45~0.5");
        List<Map<String,String>> source = new ArrayList<>();
        Map<String,String> product = null;
        for(String pro : products){
            product = new HashMap<>();
            String [] proArray = pro.split("~");
            int focus = 0;int range = 0;
            for(RedoAllPctVo var : redoAllPcts){
                if(null == var)
                    continue;
                String redoAllPctStr = StringUtils.isEmpty(var.getRedoAllPct()) ? "0" : var.getRedoAllPct();
                double redoAllPct = Double.valueOf(redoAllPctStr);
                String emphasisStr = StringUtils.isEmpty(var.getEmphasis()) ? "0" : var.getEmphasis();
                int emphasis = Integer.valueOf(emphasisStr);
                if (redoAllPct >= Double.valueOf(proArray[0])
                        && redoAllPct < Double.valueOf(proArray[1])){
                    range++;
                    if (emphasis == 1)
                        focus++;
                }
            }
            product.put("product",pro);
            product.put("focus",String.valueOf(focus));
            product.put("range",String.valueOf(range));
            source.add(product);
        }

        RedoAllPctPillar pillar = RedoAllPctPillar.builder()
                .dimensions(dimensions)
                .source(source)
                .build();
        return ResultEntity.success(pillar);
    }
}
