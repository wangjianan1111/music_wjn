package com.bjfu.notation_jh.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

/**
 * 调用远程接口的工具类
 *
 * @Author john
 * @create 2020/12/3 21:15
 */
public class HttpUtil {


    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * POST请求
     *
     * @param APIName
     * @return
     * @throws IOException
     */
    public static JSONObject doPost(String APIName, String param) throws IOException {
        LOGGER.info("==============进入doPost请求================");
        //读取配置文件的URL
        Properties properties = new Properties();
//        URL fileURL = HttpUtil.class.getClassLoader().getResource("api.properties");
//        properties.load(new FileInputStream(new File(fileURL.getFile())));
        Resource resource = new ClassPathResource("/api.properties");
        InputStream inputStream = resource.getInputStream();
        properties.load(inputStream);
        String API = properties.getProperty(APIName);
//        HttpClient client = new DefaultHttpClient();
        // HttpClient client = HttpClientBuilder.create().build();
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(API);
        JSONObject jsonObject = new JSONObject();
        try {
            StringEntity s = new StringEntity(param, Charset.forName("UTF-8"));
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");// 发送json数据需要设置contentType
            // 设置请求的报文头部的编码
            post.setHeader(new BasicHeader("Content-Type", "application/json; charset=utf-8"));
            // 设置期望服务端返回的编码
            post.setHeader(new BasicHeader("Accept", "application/json"));
            post.setEntity(s);
            LOGGER.info("==============开始执行请求================");
            // 数据连接时间
//            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
            // 数据传输时间
//            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);
            HttpResponse res = client.execute(post);
            if (res.getStatusLine().getStatusCode() == 200) {
                // HttpEntity entity = (HttpEntity) res.getEntity();
                String result = EntityUtils.toString(res.getEntity());// 返回json格式：
                jsonObject = JSONObject.parseObject(result);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    /**
     * 返回API调用结果
     *
     * @param APIName 接口在api.properties中的名称
     * @param params  访问api所需的参数及参数值
     * @return 此处返回的是JSON格式的数据
     */
    public static String API(String APIName, Map<String, Object> params) {
        String content = "";
        //请求结果
        CloseableHttpResponse response = null;
        //实例化httpclient
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            //读取配置文件的URL
            Properties properties = new Properties();
//            URL fileURL = HttpUtil.class.getClassLoader().getResource("api.properties");
//            properties.load(new FileInputStream(new File(fileURL.getFile())));
            Resource resource = new ClassPathResource("/api.properties");
            InputStream inputStream = resource.getInputStream();
            properties.load(inputStream);
            String API = properties.getProperty(APIName);
            //构造url请求
            StringBuilder url = new StringBuilder(API);
            if (params != null && params.size() > 0) {
                url.append("?");
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    url.append(entry.getKey() + "=" + entry.getValue() + "&");
                }
                url.substring(0, url.length() - 1);
            }
            //实例化get方法
            HttpGet httpget = new HttpGet(url.toString());
            //执行get请求
            response = httpclient.execute(httpget);
            if (response.getStatusLine().getStatusCode() == 200) {
                content = EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public Map<String, Object> ocrDiscern(String filePath, String url) throws IOException {
        //读取配置文件的URL
        Properties properties = new Properties();
        Resource resource = new ClassPathResource("/api.properties");
        InputStream inputStream = resource.getInputStream();
        properties.load(inputStream);
//        URL fileURL = HttpUtil.class.getClassLoader().getResource("api.properties");
//        properties.load(new FileInputStream(new File(fileURL.getFile())));
        String API = properties.getProperty(url);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("multipart/form-data");
        headers.setContentType(type);
        //设置请求体，注意是LinkedMultiValueMap
        FileSystemResource fileSystemResource = new FileSystemResource(filePath);
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        //设置发送文件和其它参数
        form.add("file", fileSystemResource);
//        form.add("can", vo.getCan());
        HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.postForObject(API, files, String.class);
        Map<String, Object> retMap = new HashMap<>();
        JSONObject object = JSONObject.parseObject(result);
        int code = object.getInteger("code");
        retMap.put("code", code);
        if (code >= 20000 && code < 30000) {
            JSONObject obj = (JSONObject) object.get("data");
            String file_id = obj.getString("id");
            retMap.put("file_id", file_id);
        } else {
            String message = object.getString("message");
            retMap.put("message", message);
        }
        return retMap;
    }

    /*
     * HttpClient Get请求带参数
     */
    public JSONObject doGet(String param, String url) {
        LOGGER.info("=============get请求开始==========");
        //读取配置文件的URL
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
        String API = properties.getProperty(url);

//		1、创建httpClient
        CloseableHttpClient client = HttpClients.createDefault();
//		2、封装请求参数
        List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("id", param));

        //3、转化参数
        String params = null;
        try {
            params = EntityUtils.toString(new UrlEncodedFormEntity(list, Consts.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(params);
        //4、创建HttpGet请求
        HttpGet httpGet = new HttpGet(API + "?" + params);
        CloseableHttpResponse response = null;
        try {
            response = client.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //5、获取实体
        org.apache.http.HttpEntity entity = response.getEntity();
        //将实体装成字符串
        String string = null;
        JSONObject jsonObject = new JSONObject();
        try {
            string = EntityUtils.toString(entity);
            jsonObject = JSONObject.parseObject(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(string);

        try {
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("=============get请求结束==========");
        return jsonObject;
    }


    /**
     * 下载远程文件并保存到本地
     *
     * @param remoteFilePath-远程文件路径
     * @param localFilePath-本地文件路径（带文件名）
     * http://q5qobmi0y.bkt.clouddn.com/01.jpg
     */
    public void downloadFile(String remoteFilePath, String localFilePath) {
        URL urlfile = null;
        HttpURLConnection httpUrl = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File f = new File(localFilePath);
        try {
            urlfile = new URL(remoteFilePath);
            httpUrl = (HttpURLConnection) urlfile.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream(f));
            int len = 2048;
            byte[] b = new byte[len];
            while ((len = bis.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            bos.flush();
            bis.close();
            httpUrl.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
