package com.lt.service;

import com.lt.entity.LtMenu;
import com.lt.mapper.MenuMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Max;
import java.util.List;

/**
 * @author gaijf
 * @description
 * @date 2020/3/17
 */
@Slf4j
@Service
public class MenuService {

    @Autowired
    MenuMapper menuMapper;

    public void queryAllMenus(){
        List<LtMenu> menus = menuMapper.queryAllMenus();
    }

    private void assemblyMenuTree(List<LtMenu> menus){
        for (LtMenu menu : menus) {
            if (menu.getPid() == null){
                continue;
            }
        }
    }
}
