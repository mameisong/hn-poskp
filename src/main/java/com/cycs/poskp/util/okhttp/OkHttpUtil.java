/**
 * Copyright (c) 北京畅易财税科技有限公司 2019
 * 本著作物的著作权归北京畅易财税有限公司所有
 */
package com.cycs.poskp.util.okhttp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * OKHttp 工具类
 * <p>
 * 提供okhttp常用的请求方式，解析
 */
@Component
public class OkHttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(OkHttpUtil.class);

    /**
     * application/json; charset=utf-8
     */
    private static final String APPLICATION_JSON = "application/json; charset=utf-8";
    /**
     * application/xml; charset=utf-8
     */
    private static final String APPLICATION_XML = "application/xml; charset=utf-8";
    /**
     * application/octet-stream
     */
    @SuppressWarnings("unused")
	private static final String APPLICATION_STREAM = "application/octet-stream";

    @Autowired
    private OkHttpClient okHttpClient;

    /**
     * 根据map获取get请求参数
     *
     * @param queries
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public StringBuffer getQueryString(String url, Map<String, String> queries) {
        StringBuffer sb = new StringBuffer(url);
        if (queries != null && queries.keySet().size() > 0) {
            boolean firstFlag = true;
            Iterator iterator = queries.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry<String, String>) iterator.next();
                if (firstFlag) {
                    sb.append("?" + entry.getKey() + "=" + entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&" + entry.getKey() + "=" + entry.getValue());
                }
            }
        }
        return sb;
    }

    /**
     * 调用okhttp的newCall方法
     *
     * @param request
     * @return
     */
    private String exec(Request request) {
        logger.info(" ===> ok-http pool, connectionCount:{}, idleConnectionCount:{}",
                okHttpClient.connectionPool().connectionCount(), okHttpClient.connectionPool().idleConnectionCount());
        logger.debug(" ===> ok-http request, method:{}, port:{}, https:{}, url:{}",
                request.method(), request.url().port(), request.isHttps(), request.url().uri());
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
//            int status = response.code();


            logger.debug(" <=== ok-http response, message : {}", response.message());
            String body = null == response.body() ? null : response.body().string();
            logger.debug(" <=== ok-http response, code : {}, body : {}",
                    response.code(), StringUtils.isBlank(body) ? "body is empty" : body);
            if (response.isSuccessful()) {
                return body;
            }
        } catch (Exception e) {
            logger.error(" <=== ok-http exec error {}", e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return "";
    }

    /**
     * get 表单
     *
     * @param url     请求的url
     * @param queries 请求的参数，浏览器中 ? 后面的数据，没有可以传null
     * @return
     */
    public String getForm(String url, Map<String, String> queries) {
        StringBuffer sb = getQueryString(url, queries);
        Request request = new Request.Builder()
                .url(sb.toString())
                .build();
        return exec(request);
    }

    /**
     * post 表单
     *
     * @param url    请求的url
     * @param params post form 提交的参数
     * @return
     */
    public String postForm(String url, Map<String, String> params) {
        Request request = new Request.Builder()
                .url(url)
                .post(getBuilder(params).build())
                .build();
        return exec(request);
    }

    /**
     * Post请求发送JSON数据....
     * <pre>
     *     {"name":"zhangsan","pwd":"123456"}
     * </pre>
     *
     * @param url  请求Url
     * @param json 请求的JSON
     * @return String 请求回调
     */
    public String postJson(String url, String json) {
        return post(url, json, APPLICATION_JSON);
    }

    /**
     * Post请求发送xml数据....
     *
     * @param url 请求Url
     * @param xml 请求的xmlString
     * @return String 请求回调
     */
    public String postXml(String url, String xml) {
        return post(url, xml, APPLICATION_XML);
    }

    /**
     * Post请求发送数据....
     * 参数一：请求Url
     * 参数二：请求的body
     * 参数三：请求回调
     */
    public String post(String url, String body, String contentType) {
        RequestBody requestBody = RequestBody.create(MediaType.parse(contentType), body);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return exec(request);
    }

    /**
     * 下载文件
     *
     * @param url 请求接口地址
     * @param file 现在文件
     * @return 接口返回值
     * 该方法前端以formData格式传入，包括文件和参数，可一起传入。
     */
    public void downloadFilePost(String url, Map<String, String> params, File file) {
        Response response = null;
        try {
            response = okHttpClient.newCall(
            		new Request.Builder().url(url)
            			//.addHeader("Connection", "close")
            			.post(getBuilder(params).build()).build())
            		.execute();
            if (response.isSuccessful()) {
            	writeFile(response, file);
            }
        } catch (Exception e) {
            logger.error("okhttp3 put error >> ex = {}", e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
        
        
//        .enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                logger.error("下载失败", e);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    writeFile(response, file);
//                }
//            }
//        });
    }

    /**
     * 组装FormBody.Builder
     * @param params 请求参数
     * @return FormBody.Builder 返回请求参数Builder
     */
    private FormBody.Builder getBuilder(Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        //添加参数
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
        return builder;
    }

    /**
     * 写入文件
     * @param response 响应体
     * @param file 要保存的文件
     */
    private void writeFile(Response response, File file) {
        try(
        	FileOutputStream fos = new FileOutputStream(file);
            InputStream is = response.body().byteStream()) {
            byte[] bytes = new byte[1024];
            int len;
            while ((len = is.read(bytes, 0, 1024)) != -1) {
                fos.write(bytes, 0, len);
            }
            fos.flush();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        logger.debug("下载成功");
    }
}
