package com.hik.iothub.util;

import okhttp3.*;
import okio.BufferedSink;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 基于okhttp3封装的HTTP请求处理的工具类<br>
 */
public class OkhttpUtils {
    private static final Logger logger = LoggerFactory.getLogger(OkhttpUtils.class);

    /**
     * json类型
     */
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    /**
     * OkHttpClient使用单例, 全局统一设置
     * 读取超时时间30秒
     * 写的超时时间30秒
     * 连接超时时间5秒
     */
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(30L, TimeUnit.SECONDS)
            .writeTimeout(30L, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)
            .build();

    /**
     * OkhttpUtils使用单实例,避免创建其它实例
     */
    private OkhttpUtils() {
    }

    /**
     * Http GET请求处理<br>
     *
     * @param getUrl: GET请求的URL
     */
    public static String doGet(String getUrl) {
        logger.info("OkhttpUtils execute GET request: {}", getUrl);
        String result = null;
        Response response = null;
        try {
            // 构造请求
            Request request = new Request.Builder().url(getUrl).build();
            response = okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                result = response.body().string();
            }
        } catch (IOException e) {
            logger.error("OkhttpUtils.doGet IOException {}", e);
        }finally {
            if(response !=null){
                response.close();
            }
        }
        return result;
    }

    public static int doPut(String putUrl) {
        logger.info("OkhttpUtils execute put request: {}", putUrl);
        int code = -1;
        Response response = null;
        try {
            // 构造请求
            RequestBody requestBody = new RequestBody() {
                @Override
                public MediaType contentType() {
                    return null;
                }

                @Override
                public void writeTo(BufferedSink bufferedSink) throws IOException {

                }
            };
            Request request = new Request.Builder().url(putUrl).put(requestBody).build();
            response = okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                code = response.code();
            }
        } catch (IOException e) {
            logger.error("OkhttpUtils.doPut IOException {}", e);
        }finally {
            if(response !=null){
                response.close();
            }
        }
        return code;
    }

    /**
     * Http GET请求处理<br>
     *
     * @param url: GET请求的URL
     * @param params: GET请求的参数
     */
    public static String doGet(String url,Map<String,Object> params) {
        logger.info("OkhttpUtils execute GET request: {}", url);
        String getUrl = url + parseGetParams(params);
        String result = null;
        Response response = null;
        try {
            // 构造请求
            Request request = new Request.Builder().url(getUrl).build();
            response = okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                result = response.body().string();
            }
        } catch (IOException e) {
            logger.error("OkhttpUtils.doGet IOException {}", e);
        }finally {
            if(response !=null){
                response.close();
            }
        }
        return result;
    }

    /**
     * Http GET请求处理<br>
     *
     * @param url: GET请求的URL
     * @param userName: 认证用户名
     * @param passWord: 认证密码
     */
    public static String doGet(String url, String userName, String passWord) {
        logger.info("OkhttpUtils execute GET request: {}", url);
        String result = null;
        Response response = null;
        try {
            // 构造请求
            Request request = new Request.Builder().url(url)
                    .addHeader("Authorization",getHeader(userName,passWord))
                    .build();
            response = okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                result = response.body().string();
            }
        } catch (IOException e) {
            logger.error("OkhttpUtils.doGet IOException {}", e);
        }finally {
            if(response !=null){
                response.close();
            }
        }
        return result;
    }

    /**
     * Http POST请求form-data数据处理
     *
     * @param postUrl
     * @param paramsMap
     * @return
     */
    public static String doPostFormData(String postUrl, Map<String, String> paramsMap) {
        logger.info("OkhttpUtils execute POST request, URL={}, data={}", postUrl, paramsMap);
        String result = null;
        Response response = null;
        try {
            FormBody.Builder builder = new FormBody.Builder();
            for (Map.Entry<String,String> v : paramsMap.entrySet()) {
                builder.add(v.getKey(), v.getValue());
            }
            RequestBody formBody = builder.build();
            Request request = new Request.Builder().url(postUrl)
                    .addHeader("Content-Type", "application/json")
                    .post(formBody).build();
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                result = response.body().string();
            } else {
                logger.error("http调用返回异常：" + response.code());
            }
        } catch (IOException e) {
            logger.error("OkhttpUtils.doPost IOException {}", e);
        }finally {
            if(response !=null){
                response.close();
            }
        }
        logger.info("OkhttpUtils get result ：" + result);
        return result;
    }

    /**
     * Http POST请求json字符串数据处理<br>
     *
     * @param postUrl:  请求的URL
     * @param jsonData: 请求的json数据
     */
    public static String doPost(String postUrl, String jsonData) {
        logger.info("OkhttpUtils execute POST request, URL={}, data={}", postUrl, jsonData);
        String result = null;
        Response response = null;
        try {
            RequestBody body = RequestBody.create(JSON, jsonData);
            // 目前统一使用json格式
            Request request = new Request.Builder().url(postUrl)
                    .addHeader("Content-Type", "application/json")
                    .post(body).build();
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                result = response.body().string();
            } else {
                logger.error(jsonData + "http调用返回异常：" + response.code());
            }
        } catch (IOException e) {
            logger.error("OkhttpUtils.doPost IOException {}", e);
        }finally {
            if(response !=null){
                response.close();
            }
        }
        logger.info("OkhttpUtils get result ：" + result);
        return result;
    }

    private static String parseGetParams(Map<String,Object> params){
        String url = "?";
        for(Map.Entry<String, Object> param:params.entrySet()){
            String key = param.getKey();
            Object value = param.getValue();
            if(value!=null){
                url += key+"="+value+"&";
            }
        }
        return StringUtils.removeEnd(url,"&");
    }

    private static String getHeader(String userName, String passWord) {
        String auth = userName + ":" + passWord;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
        return "Basic " + new String(encodedAuth);
    }
}
