package com.lt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lt.entity.LtMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gaijf
 * @description
 * @date 2020/3/18
 */
public class MenuTest {

    public static void main(String[] args) {

        List<Node> nodes = new ArrayList<>();
        //模拟数据库存储树结构
        nodes.add(new Node("1","root",1,"0"));
        nodes.add(new Node("2","a",1,"1"));
        nodes.add(new Node("3","b",1,"1"));
        nodes.add(new Node("4","c",1,"1"));
        nodes.add(new Node("5","d",1,"2"));
        nodes.add(new Node("6","e",1,"2"));
        nodes.add(new Node("7","f",1,"3"));
        nodes.add(new Node("8","g",1,"7"));
        System.out.println("result:" + JSON.toJSONString(menuTrees(nodes)));
        List<Node> list = menuTrees(nodes);
        Node node = new Node("1","root",1,"0");
        List<Node> childMenu = new ArrayList<Node>();
        List<Node> listAny = treeMenuList(nodes,"7",childMenu);
        System.out.println(JSON.toJSONString(listAny));
//        System.out.println("result:" + JSON.toJSONString(parseTree(nodes)));
//
//        System.out.println("child:"+JSON.toJSONString(getValues ("7",nodes)));
    }
    public static List<Node> treeMenuList(List<Node> menuList, String pid,List<Node> childMenu) {
        for (Node mu : menuList) {
            //遍历出父id等于参数的id，add进子节点集合
            if (mu.getParentId().equals(pid)) {
                //递归遍历下一级
                childMenu.add(mu);
                treeMenuList(menuList, mu.getId(),childMenu);
            }
        }
        return childMenu;
    }

    /**
     * 递归创建树形结构
     */
    public static List<Node> menuTrees(List<Node> menus){
        List<Node> treeMenus =new  ArrayList<Node>();
        for(Node menuNode : menus) {
            if(!menuNode.getParentId().equals("0"))
                continue;
            menuNode=buildTree(menuNode,menus);
            treeMenus.add(menuNode);
        }
        return treeMenus;
    }

    private static Node buildTree(Node menuNode,List<Node> menus){
        for(Node item : menus) {
            if(item.getParentId().equals(menuNode.getId())) {
                menuNode.getChildNodes().add(buildTree(item,menus));
            }
        }
        return menuNode;
    }
}


class Node{
    public String id ;
    public String name;
    public Integer type;
    public String parentId;
    public List<Node> childNodes = new ArrayList<>();

    public Node(String id, String name, Integer type, String parentId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.parentId = parentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<Node> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(List<Node> childNodes) {
        this.childNodes = childNodes;
    }
}
