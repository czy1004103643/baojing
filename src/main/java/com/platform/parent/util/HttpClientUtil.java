package com.platform.parent.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.platform.parent.config.WechatConfig;
import com.platform.parent.mybatis.bean.GroupPurchase;
import com.platform.parent.mybatis.bean.GroupPurchaseMember;
import com.platform.parent.wxpay.WXPayConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpClientUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    @Autowired
    WechatConfig wechatConfig;

    public static void setBodyParameter(String sb, HttpURLConnection conn)
            throws IOException {
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        out.writeBytes(sb);
        out.flush();
        out.close();
    }

    public static HttpURLConnection CreatePostHttpConnection(String uri) throws MalformedURLException,
            IOException, ProtocolException {
//        URL url = new URL(uri);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setUseCaches(false);
//        conn.setDoInput(true);
//        conn.setDoOutput(true);
//        conn.setRequestMethod("POST");
//        conn.setInstanceFollowRedirects(true);
//        conn.setConnectTimeout(30000);
//        conn.setReadTimeout(30000);
//        conn.setRequestProperty("Content-Type","application/json");
//        conn.setRequestProperty("Accept-Charset", "utf-8");
//        conn.setRequestProperty("contentType", "utf-8");
        return CreatePostHttpConnection(uri, null);
    }

    public static HttpURLConnection CreatePostHttpConnection(String uri, JSONObject body) throws MalformedURLException,
            IOException, ProtocolException {
        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setInstanceFollowRedirects(true);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);
        conn.setRequestProperty("Content-Type","application/json");
        conn.setRequestProperty("Accept-Charset", "utf-8");
        conn.setRequestProperty("contentType", "utf-8");

        if (body != null)
        {
            conn.setRequestProperty("accept","application/json");
            conn.setRequestProperty("Content-Length", String.valueOf(body.toString().getBytes("utf-8").length));
            DataOutputStream dos=new DataOutputStream(conn.getOutputStream());
            dos.write(body.toString().getBytes("utf-8"));
            dos.flush();
            dos.close();
        }
        return conn;
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;
    }

    public static AuthToken getAuthToken(String appId, String appSecret, String code){
        AuthToken vo = null;
        try {
            String uri = "https://api.weixin.qq.com/sns/oauth2/access_token?";
            StringBuffer url = new StringBuffer(uri);
            url.append("appid=").append(appId);
            url.append("&secret=").append(appSecret);
            url.append("&code=").append(code);
            url.append("&grant_type=").append("authorization_code");
            logger.info("In getAuthToken use url {}.", url);

            HttpURLConnection conn = CreatePostHttpConnection(url.toString());
            InputStream input = null;

            if (conn.getResponseCode() == 200) {
                input = conn.getInputStream();
            } else {
                input = conn.getErrorStream();
            }

            String result = new String(HttpClientUtil.readInputStream(input),"utf-8");
            logger.info("In getAuthToken got result {}.", result);
            vo = JSON.parseObject(result,AuthToken.class);
        } catch (Exception e) {
            logger.error("getAuthToken error", e);
        }

        logger.info("In getAuthToken got auth token {}.", vo);
        return vo;
    }

    public static AuthToken getAuthToken(HttpServletRequest request, String code, String channel)
    {
        HttpSession session = request.getSession();
        AuthToken token = (AuthToken)session.getAttribute("token");;
        if (token == null)
        {
            token = HttpClientUtil.getAuthToken(WXConfig.appid, WXConfig.appsecret, code);
            session.setAttribute("token", token);
            session.setMaxInactiveInterval(7200);
            logger.info("Session not containing token, retrieved and saved token to session");
        }

        logger.info("Got token : {}", token);

        if(channel != null)
        {
            // only save channel to session for group purchase create and join
            session.setAttribute("channel", channel);
            logger.info("Save passed in channel {} to session", channel);
        }

        return token;
    }

    public static UserInfo getUserInfo(String accessToken, String openId){
        UserInfo vo = null;
        try {
            String uri = "https://api.weixin.qq.com/sns/userinfo?";
            StringBuffer url = new StringBuffer(uri);
            url.append("access_token=").append(accessToken);
            url.append("&openid=").append(openId);
            url.append("&lang=zh_CN");
            logger.info("In getUserInfo use url {}.", url);

            HttpURLConnection conn = CreatePostHttpConnection(url.toString());
            InputStream input = null;

            if (conn.getResponseCode() == 200) {
                input = conn.getInputStream();
            } else {
                input = conn.getErrorStream();
            }

            String result = new String(HttpClientUtil.readInputStream(input),"utf-8");
            logger.info("In getUserInfo got result {}.", result);
            vo = JSON.parseObject(result,UserInfo.class);
        } catch (Exception e) {
            logger.error("getUserInfo error", e);
        }

        logger.info("In getUserInfo got userinfo {}.", vo);
        return vo;
    }

    public static JSONObject getRequestBody(Map<String, String> requestBodyMap)
    {
        logger.info("In getRequestBody got requestBodyMap {}.", requestBodyMap);
        JSONObject requestBody = new JSONObject();
        if (requestBodyMap == null || requestBodyMap.isEmpty())
        {
            return requestBody;
        }

        for(Map.Entry<String, String> entry: requestBodyMap.entrySet())
        {
            requestBody.put(entry.getKey(), entry.getValue());
        }

        logger.info("In getRequestBody got requestBody {}.", requestBody);


        return requestBody;
    }

    public static JSONObject getRequestBodyData(Map<String, String> requestBodyMap)
    {
        logger.info("In getRequestBodyData got requestBodyMap {}.", requestBodyMap);
        JSONObject requestBody = new JSONObject();
        if (requestBodyMap == null || requestBodyMap.isEmpty())
        {
            return requestBody;
        }

        for(Map.Entry<String, String> entry: requestBodyMap.entrySet())
        {
            JSONObject embedded = new JSONObject();
            embedded.put("value", entry.getValue());
            embedded.put("color", "#173177"); // default color

            requestBody.put(entry.getKey(), embedded);
        }

        logger.info("In getRequestBodyData got requestBodyData {}.", requestBody);


        return requestBody;
    }

    public static String getRequestBodyStr(Map<String, String> requestBodyMap)
    {
        logger.info("In getRequestBody got requestBodyMap {}.", requestBodyMap);
        StringBuilder requestBody = new StringBuilder();
        if (requestBodyMap == null || requestBodyMap.isEmpty())
        {
            return requestBody.toString();
        }

        for(Map.Entry<String, String> entry: requestBodyMap.entrySet())
        {
            if (requestBody.length() != 0)
            {
                requestBody.append("&");
            }
            requestBody.append(entry.getKey()).append("=").append(entry.getValue());
        }

        logger.info("In getRequestBody got requestBody {}.", requestBody);


        return requestBody.toString();
    }


    public static Map<String, String> getRequestMap(GroupPurchase groupPurchase, GroupPurchaseMember gpm, String template_id, String channel)
    {
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("touser", gpm.getOpenid());//"oz2uQ0-7SkoqBGruXb5jGHqyfX1E");
        requestMap.put("template_id", template_id);//"qodDflTG_HcHUKtqO3cvUYMRzXz4pHyS1LkZTa0rIsA");
        String url = "";

        if (WXPayConstants.MESSAGE_TEMPLATE_PURCHASE_GROUP == template_id && groupPurchase.isStaticContent())
        {
            url = ""; //TODO return course content URL
        }
        else
        {
            url = "http://www.mxjzq.com/mingxiao/user/info?id=" + groupPurchase.getCourseId() + "&channel=" + channel;
        }
        requestMap.put("url", url);

        return requestMap;


//        {"template":"{\"template_list\":[{\"template_id\":\"qodDflTG_HcHUKtqO3cvUYMRzXz4pHyS1LkZTa0rIsA\",
// \"title\":\"订阅模板消息\",\"primary_industry\":\"\",\"deputy_industry\":\"\",\"content\":\"{{content.DATA}}\",\"example\":\"\"}]}",
// "message":"成功","status":"0"}


        //        {
//            "touser":"OPENID",
//                "template_id":"ngqIpbwh8bUfcSsECmogfXcV14J0tQlEpBO27izEYtY",
//                "url":"http://weixin.qq.com/download",
//                "miniprogram":{
//            "appid":"xiaochengxuappid12345",
//                    "pagepath":"index?foo=bar"
//        },
//            "data":{
//            "first": {
//                "value":"恭喜你购买成功！",
//                        "color":"#173177"
//            },
//            "keynote1":{
//                "value":"巧克力",
//                        "color":"#173177"
//            },
//            "keynote2": {
//                "value":"39.8元",
//                        "color":"#173177"
//            },
//            "keynote3": {
//                "value":"2014年9月22日",
//                        "color":"#173177"
//            },
//            "remark":{
//                "value":"欢迎再次购买！",
//                        "color":"#173177"
//            }
//        }
//        }

    }

    public static String getChannel(HttpServletRequest request)
    {
        if (request == null)
        {
            logger.info("getChannel got NULL http reuqest");
            return null;
        }

        HttpSession session = request.getSession();
        String channel = (String)session.getAttribute("channel");
        logger.info("getChannel got channel {} from session", channel);

        return channel;
    }

    public static String getOutTradeNo(HttpServletRequest request)
    {
        if (request == null)
        {
            logger.info("getOutTradeNo got NULL http reuqest");
            return null;
        }

        HttpSession session = request.getSession();
        String outTradeNo = (String)session.getAttribute("outTradeNo");
        logger.info("getOutTradeNo got outTradeNo {} from session", outTradeNo);

        return outTradeNo;
    }

    public static void saveOutTradeNo(HttpServletRequest request, String outTradeNo)
    {
        if (request == null)
        {
            logger.info("saveOutTradeNo got NULL http reuqest");
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("outTradeNo", outTradeNo);
        logger.info("saveOutTradeNo saved outTradeNo {} to session", outTradeNo);
    }

    //state is courseId_channel, which is passed in url parameter for code4info request. for example: http://www.mxjzq.com/H5/?code=001mBC6X1K7HIS0m6q4X15Gz6X1mBC66&state=8_3_groupmessage
    public static String[] parseUrl(String url)
    {
        if (url == null || url.isEmpty())
        {
            return null;
        }

        String token = "state=";
        int index = url.lastIndexOf(token);
        if (index == -1)
        {
            return null;
        }

        String stateValue = url.substring(index + token.length(), url.length());

        System.out.println("parseState got stateValue from url:[" + stateValue + "]");

        return parseState(stateValue);
    }

    public static String[] parseState(String state)
    {
        if (state == null || state.isEmpty())
        {
            return null;
        }

        String[] stateSplits = state.split("_");

        return stateSplits;
    }

    public static void main(String[] args)
    {
//        System.out.println(getRequestBody(getRequestMap(null, null, "")));
//
//        String test = "{\"openid\":\"openidvalue&template_id=template_id_value\"}";
////        JSONObject.parse(test);
//        System.out.println(JSONObject.parse(test));
//        int i = 2;
//        int j = (int)(2 * 0.99);
//        System.out.println(j);
//
//        String servletPrefix = "/elearning";
//        String uri = "/elearning/storyline-test/story_html5.html";
//
//        logger.info("In elearning, uri: {}", uri);
//        int prefixIndex = uri.indexOf(servletPrefix, 0);
//        int in = uri.indexOf("/mingxiao/elearning");
//        logger.info("In elearning, prefixIndex: {}, in: {}", prefixIndex, in);
//        uri = uri.substring(prefixIndex + servletPrefix.length());
//        logger.info("In elearning, uri: {}", uri);
//        uri = "/hehe" + uri;
//        logger.info("In elearning, fwd to uri: {}", uri);

        parseState("http://www.mxjzq.com/H5/?code=001mBC6X1K7HIS0m6q4X15Gz6X1mBC66&state=8_3_groupmessage");
        parseState("http://www.mxjzq.com/H5/?code=001mBC6X1K7HIS0m6q4X15Gz6X1mBC66&state=");

    }
}
