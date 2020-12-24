package com.web.common.utils;

import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator
 */
public  class OkHttpUtils {
    public final static int CONNECT_TIMEOUT =10;
    public final static int READ_TIMEOUT=10;
    public final static int WRITE_TIMEOUT=10;
     OkHttpClient client =
            new OkHttpClient.Builder()
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                    .writeTimeout(WRITE_TIMEOUT,TimeUnit.SECONDS)//设置写的超时时间
                    .connectTimeout(CONNECT_TIMEOUT,TimeUnit.SECONDS)//设置连接超时时间
                    .build();
    /**
     * HTTP POST
     * POST提交Json数据
     *
     * @param url  请求的url
     * @param json json格式数据
     * @return
     * @throws Exception
     */
    public String post(String url, String json) throws Exception {
        String result;
        if(true){
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                 result = response.body().string();

            } else {
                throw new IOException("服务器异常" + response);
            }
        }
        return result;
    }

    /**
     * HTTP GET
     *
     * @param url 请求的url
     * @return
     * @throws Exception
     */
    public String get(String url) throws Exception {
        String result;
        if(true){
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                 result = response.body().string();
                return result;
            } else {
                throw new IOException("服务器异常" + response);
            }
        }
        return result;
    }

}