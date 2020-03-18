package com.lt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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
        nodes.add(new Node("1","root",1,null));
        nodes.add(new Node("2","a",1,"1"));
        nodes.add(new Node("3","b",1,"1"));
        nodes.add(new Node("4","c",1,"1"));
        nodes.add(new Node("5","d",1,"2"));
        nodes.add(new Node("6","e",1,"2"));
        nodes.add(new Node("7","f",1,"3"));
        nodes.add(new Node("8","g",1,"7"));
        nodes.add(new Node("9","h",1,null));
//        System.out.println("result:" + JSON.toJSONString(getTree("0",nodes)));
        System.out.println("result:" + JSON.toJSONString(parseTree(nodes)));

        System.out.println("child:"+JSON.toJSONString(getValues ("7",nodes)));
    }

    /**
     * 递归创建树形结构
     */
    private static List<Node> getTree(String parentId,List<Node> nodeList) {
        List<Node> threeNodeList = new ArrayList<>() ;
        for (Node entity : nodeList) {
            String nodeId = entity.getId() ;
            String nodeParentId = entity.getParentId() ;
            if (parentId.equals(nodeParentId)) {
                List<Node> childList = getTree(nodeId,nodeList) ;
                if (childList != null && childList.size()>0){
                    entity.setChildNodes(childList);
                }
                threeNodeList.add(entity) ;
            }
        }
        return threeNodeList ;
    }

    private static String getValues (String id,List<Node> nodeList){
        return getParents (id,nodeList,new ArrayList<>(),0);
    }

    private static String getLabel (String id,List<Node> nodeList){
        return getParents (id,nodeList,new ArrayList<>(),1);
    }

    private static String getParents (String id,List<Node> nodeList,List<String> list,int sn){
        for (Node entity : nodeList) {
            if (entity.getId().equals(id)) {
                String var = 0 == sn ? entity.getId() : entity.getName();
                list.add(var);
                if(entity.getParentId() == null){
                    return JSON.toJSONString(list);
                }
                getParents(entity.getParentId(),nodeList,list,sn) ;
            }
        }
        return String.join(",",list) ;
    }


    public static List<Node> parseTree(List<Node> list){
        List<Node> result = new ArrayList<Node>();
        for (Node dict : list) {
            if(null == dict.getParentId()) {
                result.add(dict);
            }
        }
        for (Node parent : result) {
            recursiveTree(parent, list);
        }
        return result;
    }

    public static void recursiveTree(Node parent, List<Node> list) {
        for (Node menu : list) {
            if(parent.getId().equals(menu.getParentId())) {
                recursiveTree(menu, list);
                parent.getChildNodes().add(menu);
            }
        }
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
