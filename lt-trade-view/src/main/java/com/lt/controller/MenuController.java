package com.lt.controller;

import com.lt.entity.LtMenu;
import com.lt.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author gaijf
 * @description
 * @date 2020/3/18
 */
@Slf4j
@Controller
public class MenuController {

    @Autowired
    MenuService menuService;

    @GetMapping("menus")
    @ResponseBody
    public List<LtMenu> getMenus(){
        return menuService.queryAllMenus();
    }
}
