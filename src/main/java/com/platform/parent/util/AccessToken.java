package com.platform.parent.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class AccessToken {
    private static final Logger logger = LoggerFactory.getLogger(AccessToken.class);

    private static Map<String, AuthToken> accessTokenMap = new HashMap<String, AuthToken>();

    public static int time = 0;

    private static int expires_in = 7200;

    static{
        Thread t = new Thread(new Runnable(){
            public void run(){
                do{
                    time++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }while(true);
            }});
        t.start();
    }

    public static AuthToken getToken(String code){
        if(!accessTokenMap.containsKey(code)){
            send(code);
        }else if(time>expires_in){
            //当前token已经失效，从新获取信息
            send(code);
        }
        return accessTokenMap.get(code);
    }

    private static void send(String code){
        String url = WXConfig.access_token_url + "?appid=" + WXConfig.appid + "&secret=" + WXConfig.appsecret
                + "&code=" + code + "&grant_type=authorization_code";
        try
        {
            HttpURLConnection conn = HttpClientUtil.CreatePostHttpConnection(url.toString());
            InputStream input = null;

            if (conn.getResponseCode() == 200) {
                input = conn.getInputStream();
            } else {
                input = conn.getErrorStream();
            }

            String result = new String(HttpClientUtil.readInputStream(input),"utf-8");
            logger.info("In AccessToken.send got access_token result {}.", result);

            AuthToken at = JSONObject.parseObject(result, AuthToken.class);
            logger.info("In AccessToken.send got access_token {}.", at);
            accessTokenMap.put(code, at);
            time = 0;
        }
        catch (Exception e)
        {
            logger.error("In AccessToken.send got exception {}.", e.getMessage());
        }
    }
}
