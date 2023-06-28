package com.weimeizi.rfc.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName HttpUtils
 * @Description TODO
 * @Author 03126
 * @Date 2020/5/13 15:44
 **/
public class HttpUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
    private static CloseableHttpClient httpClient;

    static {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(100000)
                .setSocketTimeout(100000)
                .build();
        httpClient = HttpClients.custom()
                //.disableAutomaticRetries()
                .setDefaultRequestConfig(config)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(3, true))
                .build();
    }

    private HttpUtils() {
    }


    public static CloseableHttpClient getClient() {
        return httpClient;
    }

    public static Get get(String uri) {
        return new Get(uri);
    }

    public static Post post(String uri) {
        return new Post(uri);
    }

    /**
     * @Author 03126
     * @Description TODO 构造者模式发送Get请求
     * @Date 9:56 2020/5/21
     * @Param
     * @return
     **/
    public static class Get {
        private StringBuilder str;
        private List<NameValuePair> params = new ArrayList<>(5);
        private List<NameValuePair> headers = new ArrayList<>(5);

        public Get(String uri) {
            str = new StringBuilder(uri);
        }

        public Get head(String key, String value) {
            headers.add(new BasicNameValuePair(key, value));
            return this;
        }

        public Get param(String key, String value, Boolean isAdd) {
            if (isAdd) {
                param(key, value);
            }
            return this;
        }

        public Get param(Object param) {
            if (param==null){
                return this;
            }
            Field[] fields = param.getClass().getDeclaredFields();
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    String value = (String) field.get(param);
                    if (value != null) {
                        params.add(new BasicNameValuePair(field.getName(), value));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return this;
        }

        public Get param(String key, String value) {
            params.add(new BasicNameValuePair(key, value));
            return this;
        }

        public Return execute() {
            Post.addParam(params, str);
            HttpGet request = new HttpGet(str.toString());
            request.setHeader("Content-Type", "application/json");
            headers.forEach(header -> request.setHeader(header.getName(), header.getValue()));
            CloseableHttpClient client =HttpUtils.getClient();
            CloseableHttpResponse response = null;
            String resContent = null;
            try {
                response = client.execute(request);
                resContent = EntityUtils.toString(response.getEntity(), "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
                StringBuilder error = new StringBuilder("GET请求失败，请求头：");
                for (Header header : request.getAllHeaders()) {
                    error.append(header.getName()).append("  ").append(header.getValue()).append("\n");
                }
                error.append("请求体：").append(request.getURI()).append("\n");
                logger.error(error.toString());
                throw new RuntimeException(e);
            } finally {
                try {
                    response.close();
                } catch (IOException e) {
                    logger.error("HTTP Response关闭失败");
                    e.printStackTrace();
                }
            }
            return new Return(resContent,response);
        }

    }

    public static class Return {

        private String resContent;
        private CloseableHttpResponse response;

        public Return(String resContent, CloseableHttpResponse response) {
            if (resContent == null|| response == null) {
                throw new RuntimeException("HTTP请求失败错误，不能获取返回内容");
            }
            this.resContent = resContent;
            this.response = response;
        }

        public String returnContent() {
            return resContent;
        }

        public CloseableHttpResponse getResponse(){
            return response;
        }

        public <T> T returnObj(Class<T> t) {
            return JSON.parseObject(resContent, t);
        }

        public JSONObject returnJSONObject() {
            return JSON.parseObject(resContent);
        }
    }

    /**
     * @Author 03126
     * @Description TODO 构造者模式发送Post请求
     * @Date 9:56 2020/5/21
     * @Param
     * @return
     **/
    public static class Post {
        private StringBuilder uri;
        private List<NameValuePair> headers = new ArrayList<>(5);
        private Map<String,Object> form = new HashMap<>(5);
        private List<NameValuePair> params = new ArrayList<>(5);
        private Object body;

        public Post(String uri) {
            this.uri = new StringBuilder(uri);
        }

        public Post head(String name, String value) {
            headers.add(new BasicNameValuePair(name, value));
            return this;
        }

        public Post param(String name, String value, Boolean isAdd) {
            if (isAdd) {
                param(name, value);
            }
            return this;
        }

        public Post param(String name, String value) {
            params.add(new BasicNameValuePair(name, value));
            return this;
        }

        public Post body(Object body) {
            this.body = body;
            return this;
        }

        public Post form(String name, Object value) {
            form.put(name, value);
            return this;
        }

        public Return execute() {
            if (body != null && form.size() > 0) {
                throw new RuntimeException("不能同时有多种请求体");
            }
            CloseableHttpResponse res = null;
            addParam(params, uri);
            HttpPost httpPost = new HttpPost(uri.toString());
            headers.forEach(head -> httpPost.setHeader(head.getName(), head.getValue()));
            if (form.size() > 0) {
                String bodyJSON = JSON.toJSONString(form);
                httpPost.setEntity(new StringEntity(bodyJSON, "UTF-8"));
            }
            if (body != null) {
                String bodyJSON = JSON.toJSONString(body);
                httpPost.setEntity(new StringEntity(bodyJSON, "UTF-8"));
            }
            String resContent;
            try {
                res =HttpUtils.getClient().execute(httpPost);
                resContent = EntityUtils.toString(res.getEntity(), "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
                StringBuilder error = new StringBuilder("POST请求失败，请求头：");
                for (Header header : res.getAllHeaders()) {
                    error.append(header.getName()).append("  ").append(header.getValue()).append("\n");
                }
                error.append("请求体：").append(body).append("\n");
                logger.error(error.toString());
                throw new RuntimeException(e);
            } finally {
                try {
                    res.close();
                } catch (IOException e) {
                    logger.error("HTTP POST响应关闭失败");
                    e.printStackTrace();
                }
            }
            return new Return(resContent,res);
        }

        private static void addParam(List<NameValuePair> params, StringBuilder uri) {
            try {
                for (int i = 0; i < params.size(); i++) {
                    if (i == 0) {
                        uri.append("?").append(params.get(i).getName()).append("=").append(URLEncoder.encode(params.get(i).getValue(), "UTF-8"));
                    } else {

                        uri.append("&").append(params.get(i).getName()).append("=").append(URLEncoder.encode(params.get(i).getValue(), "UTF-8"));
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }


    }


}
