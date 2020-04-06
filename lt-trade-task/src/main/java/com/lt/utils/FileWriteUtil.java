package com.lt.utils;

import java.io.*;
import java.util.Date;

/**
 * @author gaijf
 * @description
 * @date 2020/3/31
 */
public class FileWriteUtil {

    public static void writeTXT(String path,String title,String content){
        try {
            File writename = new File(path);// 相对路径，如果没有则要建立一个新的output。txt文件
            if(!writename.exists()){
                writename.mkdirs();
            }
            String fileName = path+title+".txt";
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new FileWriter(fileName, true)));
            out.println(content);
            if (out != null) {
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getTextPath(){
        return "/home/stock/"+TimeUtil.dateFormat(new Date(),"yyyyMMdd")+"/";
//        return "E:\\zip\\"+TimeUtil.dateFormat(new Date(),"yyyyMMdd")+"\\";
    }
}
