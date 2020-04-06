package com.lt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author gaijf
 * @description
 * @date 2020/3/26
 */
@Controller
@RequestMapping("chart")
public class ChartController {

    @RequestMapping("/fund")
    public String fundChart(){
        return "trade/chart/fundChart";
    }
}
