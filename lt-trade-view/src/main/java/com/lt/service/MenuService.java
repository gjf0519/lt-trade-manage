package com.lt.service;

import com.alibaba.fastjson.JSON;
import com.lt.entity.LtMenu;
import com.lt.mapper.MenuMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<LtMenu> queryAllMenus(){
        List<LtMenu> menus = menuMapper.queryAllMenus();
        menus = menuTrees(menus);
        return menus;
    }

    public List<LtMenu> menuTrees(List<LtMenu> menus){
        List<LtMenu> treeMenus =new  ArrayList<LtMenu>();
        for(LtMenu menuNode : menus) {
            if(menuNode.getPid() != 0)
                continue;
            menuNode=buildTree(menuNode,menus);
            treeMenus.add(menuNode);
        }
        return treeMenus;
    }

    private LtMenu buildTree(LtMenu menuNode,List<LtMenu> menus){
        for(LtMenu item : menus) {
            if(item.getPid() == menuNode.getId()) {
                menuNode.getChildren().add(buildTree(item,menus));
            }
        }
        return menuNode;
    }
}
