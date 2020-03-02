package com.lt.task;

import com.alibaba.fastjson.JSON;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author gaijf
 * @description
 * @date 2019/11/3
 */
public class DownLoadThread implements Runnable{
    private List<String> codes;
    private RestTemplate restTemplate;
    private CountDownLatch latch;
    private String path;
    private String url;
    public DownLoadThread(List<String> codes, RestTemplate restTemplate,
                          CountDownLatch latch, String path,String url){
        this.codes = codes;
        this.restTemplate = restTemplate;
        this.latch = latch;
        this.path = path;
        this.url = url;
    }
    @Override
    public void run() {
        System.out.println(JSON.toJSONString(codes));
        for (String code:codes) {
            code = code.startsWith("sh") ? code.replace("sh","0"):
                    code.replace("sz","1");
            String fullUrl = getUrl(this.url,code);
            String filePath = getUrl(this.path,code);
            downloadHttp(restTemplate,fullUrl,filePath,0);
        }
        latch.countDown();
    }

    public static String getPath(String path,String code){
        return path+code+".xls";
    }

    public static String getUrl(String url,String code){
        return url+code+".xls";
    }

    public static void downloadHttp(RestTemplate restTemplate, String url, String storagePath, int next){
        ResponseEntity<byte[]> response = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<Resource> httpEntity = new HttpEntity<Resource>(headers);
            response = restTemplate.exchange(url, HttpMethod.GET,
                    httpEntity, byte[].class);
            File file = new File(storagePath);
            File fileParent = file.getParentFile();
            //判断是否存在
            if (!fileParent.exists()) {
                //创建父目录文件
                fileParent.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(response.getBody());
            fos.flush();
            fos.close();
        } catch (HttpClientErrorException e) {
            if(next < 5){
                downloadHttp(restTemplate,url,storagePath,++next);
            }
            System.out.println(url);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
