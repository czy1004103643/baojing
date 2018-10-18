package com.platform.parent.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;

public class ClientCrendentialToken {
    private static final Logger logger = LoggerFactory.getLogger(ClientCrendentialToken.class);

    private static String access_token = "";

    private static String jsapi_ticket = "";

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

    public static String getToken(){
        if("".equals(access_token)||access_token==null){
            send();
        }else if(time>expires_in){
            //当前token已经失效，从新获取信息
            send();
        }
        return access_token;
    }
    public static String getTicket(){
        if("".equals(jsapi_ticket)||jsapi_ticket==null){
            send();
        }else if(time>expires_in){
            //当前token已经失效，从新获取信息
            send();
        }
        return jsapi_ticket;
    }
    private static void send(){
        String url = WXConfig.server_token_url + "&appid=" + WXConfig.appid + "&secret=" + WXConfig.appsecret;
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
            logger.info("In send got access_token result {}.", result);

            ClientToken ct = JSONObject.parseObject(result, ClientToken.class);
            access_token = ct.getAccess_token();
            logger.info("In send got access_token {}.", access_token);

            String ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+access_token+"&type=jsapi";
            conn = HttpClientUtil.CreatePostHttpConnection(ticket_url.toString());
            if (conn.getResponseCode() == 200) {
                input = conn.getInputStream();
            } else {
                input = conn.getErrorStream();
            }
            result = new String(HttpClientUtil.readInputStream(input),"utf-8");
            logger.info("In send got jsapi_ticket result {}.", result);
            Ticket ticket = JSONObject.parseObject(result, Ticket.class);
            jsapi_ticket = ticket.getTicket();
            logger.info("In send got jsapi_ticket {}.", jsapi_ticket);
            time = 0;
        }
        catch (Exception e)
        {
            logger.error("In send got exception {}.", e.getMessage());
        }
    }
}
