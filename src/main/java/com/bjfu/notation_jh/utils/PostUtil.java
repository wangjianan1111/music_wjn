package com.bjfu.notation_jh.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * @Author john
 * @create 2021/1/13 23:54
 */
public class PostUtil {

    /**
     * 调用歌声合成接口
     * @param APIName
     * @param param
     * @return
     */
    public String officialFileUpload(String APIName, String param){
        Properties properties = new Properties();
//        URL fileURL = HttpUtil.class.getClassLoader().getResource("api.properties");
        Resource resource = new ClassPathResource("/api.properties");
        try {
//            properties.load(new FileInputStream(new File(fileURL.getFile())));
            InputStream inputStream = resource.getInputStream();
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String API = properties.getProperty(APIName);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(API);
        String result = "";
        try {
            FileBody bin = new FileBody(new File(param));
            MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
            reqEntity.addPart("file", bin);
            HttpEntity httpEntity = reqEntity.build();
            httpPost.setEntity(httpEntity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            httpPost.releaseConnection();
        }
        return result;
    }
}
