package com.platform.parent.util;

import com.alibaba.fastjson.JSONObject;
import com.platform.parent.config.WechatConfig;
import com.platform.parent.mybatis.bean.GroupPurchase;
import com.platform.parent.mybatis.bean.GroupPurchaseMember;
import com.platform.parent.wxpay.WXPayConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.*;

public class WechatUtil {
    private static final Logger logger = LoggerFactory.getLogger(WechatUtil.class);

    @Autowired
    WechatConfig wechatConfig;

    public static HashMap<String, String> jsSDK_Sign(String url, String appId) throws Exception {
//        String nonce_str = create_nonce_str();
        String nonce_str = getRandomStringByLength(32);
        long timestamp = new Date().getTime()/1000;
        String jsapi_ticket = ClientCrendentialToken.getTicket();
        // 注意这里参数名必须全部小写，且必须有序
        String  sign = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str
                + "&timestamp=" + timestamp  + "&url=" + url;

        logger.info("jsSDK_Sign string is " + sign);
//        MessageDigest cryptessageDigest.getInstance("SHA-1");
//        crypt.reset();
//        crypt.update(sign.getBytes("UTF-8"));
//        String signature = byteToHex(crypt.digest());
        String signature = new SHA1().getDigestOfString(sign.getBytes("utf-8"));
        HashMap<String, String> jssdk=new HashMap<String, String>();
        jssdk.put("appId", appId);
        jssdk.put("timestamp", "" + timestamp);
        jssdk.put("nonceStr", nonce_str);
        jssdk.put("signature", signature);
        return jssdk;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static String wechatApiGet(String url)
    {
//        if (url == null || url.isEmpty())
//        {
//            return null;
//        }
//
//        String wechatApiGetResult = null;
//        try
//        {
//            HttpURLConnection conn = HttpClientUtil.CreatePostHttpConnection(url);
//            InputStream input = null;
//            if (conn.getResponseCode() == 200) {
//                input = conn.getInputStream();
//            } else {
//                input = conn.getErrorStream();
//            }
//            wechatApiGetResult = new String(HttpClientUtil.readInputStream(input),"utf-8");
//            logger.info("wechatApiGet {} result {}.", url, wechatApiGetResult);
//        }
//        catch (Exception exception)
//        {
//            logger.info("wechatApiGet {} exception {}.", url, exception.getMessage());
//        }
        return wechatApiGet(url, null);
    }

    public static String wechatApiGet(String url, JSONObject body)
    {
        if (url == null || url.isEmpty())
        {
            return null;
        }

        String wechatApiGetResult = null;
        try
        {
            HttpURLConnection conn = HttpClientUtil.CreatePostHttpConnection(url, body);
            InputStream input = null;
            if (conn.getResponseCode() == 200) {
                input = conn.getInputStream();
            } else {
                input = conn.getErrorStream();
            }
            wechatApiGetResult = new String(HttpClientUtil.readInputStream(input),"utf-8");
            logger.info("wechatApiGet {} result {}.", url, wechatApiGetResult);
        }
        catch (Exception exception)
        {
            logger.info("wechatApiGet {} exception {}.", url, exception.getMessage());
        }
        return wechatApiGetResult;
    }

    public static void sendMessage(GroupPurchase gp, GroupPurchaseMember gpm, String template, Map<String, String> templateMap, String channel)
    {
        String url = WXPayConstants.MESSAGE_SEND_URL + ClientCrendentialToken.getToken();

        JSONObject bodyObject = HttpClientUtil.getRequestBody(HttpClientUtil.getRequestMap(gp, gpm, template, channel));

        JSONObject dataObject = HttpClientUtil.getRequestBodyData(templateMap);

        bodyObject.put("data", dataObject);

        logger.info("sendMessage with bodyObject {}.", bodyObject);

        WechatUtil.wechatApiGet(url, bodyObject);
    }
}
