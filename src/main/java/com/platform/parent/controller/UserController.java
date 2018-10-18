package com.platform.parent.controller;

import com.alibaba.fastjson.JSONObject;
import com.platform.parent.config.GroupPurchaseConfig;
import com.platform.parent.config.WechatConfig;
import com.platform.parent.mybatis.bean.*;
import com.platform.parent.mybatis.dao.CourseMapper;
import com.platform.parent.mybatis.service.CourseService;
import com.platform.parent.util.*;
import com.platform.parent.wxpay.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by dengb.
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    WechatConfig wechatConfig;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    Refunder refunder;

    @Autowired
    GroupPurchaseConfig gpConfig;

    @Autowired
    CourseService courseService;

    @RequestMapping("/auth")
    public String auth(HttpServletRequest request, HttpServletResponse response){
        String openidURL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
                wechatConfig.getAppid() + "&redirect_uri=http://" + wechatConfig.getServer() +
                "/mingxiao/user/code" + "&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";

        try {
            response.sendRedirect(openidURL);
        }
        catch (java.io.IOException ioException)
        {
            logger.error("IOException when redirecting " + openidURL + ": " + ioException.toString());
        }

        return "user";
    }

    @RequestMapping(value = "/code", method = RequestMethod.GET)
    public @ResponseBody Object code(HttpServletRequest request, HttpServletResponse response){
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        logger.info("getOpenid with code: " + code + ", and state: " + state);

//        AuthToken token = HttpClientUtil.getAuthToken(wechatConfig.getAppid(), wechatConfig.getAppsecret(), code);
        AuthToken token = HttpClientUtil.getAuthToken(request, code, null);

        logger.info("token is {}.", token);
        JSONObject result = new JSONObject();
        result.put("status","0");
        result.put("message","成功");
        JSONObject data = new JSONObject();
        data.put("openid", token.getOpenid());
        data.put("unionid", token.getUnionid());
        data.put("errcode", token.getErrcode());
        data.put("errmsg", token.getErrmsg());
        result.put("data", data);

        return result;
    }

    @RequestMapping("/info1")
    public String info1(HttpServletRequest request, HttpServletResponse response){
        String openidURL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
                wechatConfig.getAppid() + "&redirect_uri=http://" + wechatConfig.getServer() +
                "/mingxiao/user/code4info" + "&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";

        try {
            response.sendRedirect(openidURL);
        }
        catch (java.io.IOException ioException)
        {
            logger.error("IOException when redirecting " + openidURL + ": " + ioException.toString());
        }

        return "user";
    }
    
    @RequestMapping("/info0")
    public String info0(HttpServletRequest request, HttpServletResponse response){
//        String id = request.getParameter("id");
//        logger.info("info with id: " + id);

        StringBuilder requestParams = new StringBuilder();
        Enumeration<String> em = request.getParameterNames();
        while (em.hasMoreElements()) {
            String name = em.nextElement();
            String value = request.getParameter(name);
            requestParams.append(requestParams.length() == 0 ? "?" : "&").append(name).append("=").append(value);
        }

        logger.info("info requestParams: " + requestParams);

//        String openidURL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
//                wechatConfig.getAppid() + "&redirect_uri=http://" + wechatConfig.getServer()
//                + (id == null ? "" : "?id=" + id)
//                + "&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";

        String openidURL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
                wechatConfig.getAppid() + "&redirect_uri=http://" + wechatConfig.getServer()
                + (requestParams.length() > 0 ? requestParams.toString() : "")
                + "&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";

        try {
            response.sendRedirect(openidURL);
        }
        catch (java.io.IOException ioException)
        {
            logger.error("IOException when redirecting " + openidURL + ": " + ioException.toString());
        }

        return "user";
    }

    @RequestMapping("/info")
    public String info(HttpServletRequest request, HttpServletResponse response){
//        String id = request.getParameter("id");
//        logger.info("info with id: " + id);

        StringBuilder requestParams = new StringBuilder();
        StringBuilder state = new StringBuilder();
        Enumeration<String> em = request.getParameterNames();
        while (em.hasMoreElements()) {
            String name = em.nextElement();
            String value = request.getParameter(name);
            requestParams.append(requestParams.length() == 0 ? "?" : "&").append(name).append("=").append(value);

            state.append(state.length() == 0 ? value : "_" + value);
        }

        logger.info("info requestParams: " + requestParams + ", state: " + state);

//        String openidURL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
//                wechatConfig.getAppid() + "&redirect_uri=http://" + wechatConfig.getServer()
//                + (id == null ? "" : "?id=" + id)
//                + "&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";

        String openidURL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
                wechatConfig.getAppid() + "&redirect_uri=http://" + wechatConfig.getServer()
                + wechatConfig.getDomainpath()
//                + (requestParams.length() > 0 ? requestParams.toString() : "")
                + "&response_type=code&scope=snsapi_userinfo&state=" + state + "#wechat_redirect";

        try {
            response.sendRedirect(openidURL);
        }
        catch (java.io.IOException ioException)
        {
            logger.error("IOException when redirecting " + openidURL + ": " + ioException.toString());
        }

        return "user";
    }


    @RequestMapping(value = "/code4info1", method = RequestMethod.GET)
    public @ResponseBody Object code4info1(HttpServletRequest request, HttpServletResponse response){
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        logger.info("getOpenid with code: " + code + ", and state: " + state);

//        AuthToken token = HttpClientUtil.getAuthToken(wechatConfig.getAppid(), wechatConfig.getAppsecret(), code);
        AuthToken token = HttpClientUtil.getAuthToken(request, code, null);

        logger.info("token is {}.", token);

        UserInfo userInfo = HttpClientUtil.getUserInfo(token.getAccess_token(), token.getOpenid());

        logger.info("userInfo is {}.", userInfo);

        JSONObject result = new JSONObject();
        result.put("status","0");
        result.put("message","成功");
        JSONObject data = new JSONObject();
        data.put("userInfo", userInfo);
        result.put("data", data);

        return result;
    }

    @RequestMapping(value = "/code4info", method = RequestMethod.GET)
    public @ResponseBody Object code4info(HttpServletRequest request, HttpServletResponse response){
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        String url = request.getParameter("url");
        logger.info("getOpenid with code: " + code + ", and state: " + state + ", and url: " + url);

//        AuthToken token = HttpClientUtil.getAuthToken(wechatConfig.getAppid(), wechatConfig.getAppsecret(), code);
//        AuthToken token = AccessToken.getToken(code); //TODO, this is cache access token?
//        HttpSession session = request.getSession();
//        AuthToken token = null;
//        if (session.isNew())
//        {
//            token = HttpClientUtil.getAuthToken(wechatConfig.getAppid(), wechatConfig.getAppsecret(), code);
//            session.setAttribute("token", token);
//            logger.info("Session is new, retrieved token {}", token);
//        }
//        else
//        {
//            token = (AuthToken)session.getAttribute("token");
//            logger.info("Session is old, retrieved token {}.", token);
//        }
        String[] stateParse = HttpClientUtil.parseUrl(url);
        String courseId = stateParse != null ? stateParse[0] : null;
        String channel = stateParse != null && stateParse.length >= 2 ? stateParse[1] : null;

        logger.info("In code4info, parsed courseId {} and channel {}, check course access", courseId, channel);
        AuthToken token = HttpClientUtil.getAuthToken(request, code, channel);

        UserInfo userInfo = HttpClientUtil.getUserInfo(token.getAccess_token(), token.getOpenid());

        logger.info("userInfo is {}.", userInfo);

        JSONObject result = new JSONObject();
        result.put("status","0");
        result.put("message","成功");
        JSONObject data = new JSONObject();
        data.put("userInfo", userInfo);
        result.put("data", data);
        if (userInfo != null){
            //先查询当前openid有没有H5支付记录
            if (courseMapper.getH5PaymentCount(token.getOpenid()) > 0) {
                //更新 member表,针对于H5支付
                courseMapper.updateMemberByUserInfo(token.getUnionid(),token.getOpenid(), userInfo.getNickname(), userInfo.getHeadimgurl());
            }
        }
        if(url != null)
        {
            try {
                Map<String, String> configMap = WechatUtil.jsSDK_Sign(url, wechatConfig.getAppid());

                data.put("appId", configMap.get("appId"));
                data.put("timestamp", configMap.get("timestamp"));
                data.put("nonceStr", configMap.get("nonceStr"));
                data.put("signature", configMap.get("signature"));

                logger.info("code4info configMap: " + configMap);
            } catch (Exception e) {
                logger.error("JSSDK_config with url: " + url + " failed: " + e.getMessage());
            }
        }

        return result;
    }

    @RequestMapping("/adminApplication")
    public String adminApplication(HttpServletRequest request, HttpServletResponse response){
        String openidURL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
                wechatConfig.getAppid() + "&redirect_uri=http://" + wechatConfig.getServer() +
                "/mingxiao/user/code4Admin" + "&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";

        try {
            response.sendRedirect(openidURL);
        }
        catch (java.io.IOException ioException)
        {
            logger.error("IOException when redirecting " + openidURL + ": " + ioException.toString());
        }

        return "user";
    }

    @RequestMapping(value = "/code4Admin", method = RequestMethod.GET)
    public @ResponseBody Object code4Admin(HttpServletRequest request, HttpServletResponse response){
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        String url = request.getParameter("url");
        logger.info("In code4Admin, getOpenid with code: " + code + ", and state: " + state + ", and url: " + url);

        String[] stateParse = HttpClientUtil.parseUrl(url);
        String courseId = stateParse != null ? stateParse[0] : null;
        String channel = stateParse != null && stateParse.length >= 2 ? stateParse[1] : null;

        logger.info("In code4Admin, parsed courseId {} and channel {}, check course access", courseId, channel);
        AuthToken token = HttpClientUtil.getAuthToken(request, code, channel);

        UserInfo userInfo = HttpClientUtil.getUserInfo(token.getAccess_token(), token.getOpenid());

        logger.info("In code4Admin, userInfo is {}.", userInfo);

        AdminMember am = new AdminMember();
        am.openid(userInfo.getOpenid()).unionid(userInfo.getUnionid()).nickname(userInfo.getNickname())
                .headimgurl(userInfo.getHeadimgurl()).created(new Timestamp(System.currentTimeMillis()));

        int result = this.courseService.addAdminCandidate(am);
        logger.info("In code4Admin, add admin candidate result: " + result);

        return "Your admin application result: " + result;
    }

    @RequestMapping(value = "/adminCandidates", method = RequestMethod.GET)
    public @ResponseBody Object adminCandidates(HttpServletRequest request, HttpServletResponse response){
        List<AdminMember> ams = this.courseService.findAdminCandidates();

        logger.info("adminCandidates are {}.", ams);
        JSONObject result = new JSONObject();
        result.put("status","0");
        result.put("message","成功");
        JSONObject data = new JSONObject();
        data.put("adminCandidates", ams.toString());
        result.put("data", data);

        return result;
    }

    @RequestMapping(value = "/adminCandidateDelete", method = RequestMethod.GET)
    public @ResponseBody Object adminCandidateDelete(HttpServletRequest request, HttpServletResponse response){
        String id = request.getParameter("id");

        logger.info("In adminCandidateDelete id: " + id);

        AdminMember am = this.courseService.findAdminCandidateById(Long.parseLong(id));
        logger.info("Delete admin candidate: " + am);

        int adminDeleteResult = this.courseService.deleteAdminCandidate(am);

        return "Admin candidate delete result for " + id + ": " + adminDeleteResult;
    }

    @RequestMapping(value = "/adminApprove", method = RequestMethod.GET)
    public @ResponseBody Object adminApprove(HttpServletRequest request, HttpServletResponse response){
        String id = request.getParameter("id");

        logger.info("In adminApprove id: " + id);

        AdminMember am = this.courseService.findAdminCandidateById(Long.parseLong(id));
        logger.info("Approve admin candidate: " + am);

        int adminAddResult = this.courseService.addAdminMember(am);
        int adminCandidateDeleteResult = this.courseService.deleteAdminCandidate(am);

        return "Admin apporve result for " + id + ": " + adminAddResult + "~" + adminCandidateDeleteResult;
    }

    @RequestMapping(value = "/adminMemberDelete", method = RequestMethod.GET)
    public @ResponseBody Object adminMemberDelete(HttpServletRequest request, HttpServletResponse response){
        String id = request.getParameter("id");

        logger.info("In adminMemberDelete id: " + id);

        AdminMember am = this.courseService.findAdminMemberById(Long.parseLong(id));
        logger.info("Delete admin member: " + am);

        int adminDeleteResult = this.courseService.deleteAdminMember(am);

        return "Admin delete result for " + id + ": " + adminDeleteResult;
    }


    @RequestMapping(value = "/adminMembers", method = RequestMethod.GET)
    public @ResponseBody Object adminMembers(HttpServletRequest request, HttpServletResponse response){
        List<AdminMember> ams = this.courseService.findAdminMembers();

        logger.info("adminMembers {}.", ams);
        JSONObject result = new JSONObject();
        result.put("status","0");
        result.put("message","成功");
        JSONObject data = new JSONObject();
        data.put("result", ams.toString());
        result.put("data", data);

        return result;
    }

    /**
     * @Description: 前端获取微信JSSDK的配置参数
     * @param @param response
     * @param @param request
     * @param @param url
     * @param @throws Exception
     * @author dapengniao
     * @date 2016年3月19日 下午5:57:52
     */
    @RequestMapping("/jssdk")
//    @RequestMapping(value = "/jssdk", method = RequestMethod.GET)
    public @ResponseBody Object JSSDK_config(@RequestParam(value = "url", required = true) String url) throws UnsupportedEncodingException {
        logger.info("JSSDK_config with url: " + url);
        String decodedUrl = url.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
        try
        {
            decodedUrl = URLDecoder.decode(url, "UTF-8");
            logger.info("JSSDK_config with decodedUrl: " + decodedUrl);
        }
        catch (UnsupportedEncodingException exception)
        {
            logger.error("UnsupportedEncodingException: " + exception.getMessage());
        }

        JSONObject result = new JSONObject();
        try {
            Map<String, String> configMap = WechatUtil.jsSDK_Sign(url, wechatConfig.getAppid());

            result.put("status","0");
            result.put("message","成功");
            JSONObject data = new JSONObject();
            data.put("configMap", configMap);
            result.put("data", data);
        } catch (Exception e) {
            logger.error("JSSDK_config with url: " + url + " failed: " + e.getMessage());
        }
        return result;
    }

    @RequestMapping("/prepayid")
    public @ResponseBody Object prepay_id(HttpServletRequest request, HttpServletResponse response){
        String openid = request.getParameter("openid");
//        String fee = request.getParameter("fee");
        String courseId = request.getParameter("courseId");
        String memebersCount = request.getParameter("membersCount");
        logger.info("prepay_id with openid {}, courseId {}, membersCount {}", openid, courseId, memebersCount);

        long courseIdLong = Long.parseLong(courseId);
        BigDecimal price = gpConfig.getPrice(courseIdLong, Integer.parseInt(memebersCount));
        if (price == null)
        {
            logger.error("No group purchase price available for courseId " + courseId + " and member count " + memebersCount);
            return EnumUtil.errorToJson(ErrorCode.GROUP_PURCHASE_MEMBER_CONFIG_NOT_AVAILABLE);
        }

        Course course = courseService.findCourseById(courseIdLong);
        if (course == null) {
            logger.error("Find course for courseId:{} error, no such course", courseIdLong);
            return EnumUtil.errorToJson(ErrorCode.NO_SUCH_COURSE);
        }

        int fee = price.multiply(new BigDecimal(course.getSessionCount() * 100)).intValue();
        logger.info("prepay_id with fee {}, unit is fen", fee);

        String outTradeNo = WXPayUtil.generateOutTradeNo();
        HashMap<String, String> data = new HashMap<String, String>();
//        if("12".equals(courseId)){
            data.put("body", "团购金额"); //TODO get from course.title?
//        }else {
//            data.put("body", "名校家长圈团购"); //TODO get from course.title?
//        }
        data.put("out_trade_no", outTradeNo);
        data.put("device_info", "WEB");
        data.put("fee_type", "CNY");
        data.put("total_fee", "" + fee);  //TODO get from course.price?  *100 to convert to 分？
        data.put("spbill_create_ip", request.getRemoteAddr());
        data.put("notify_url", WXPayConstants.NOTIFY_URL);
        data.put("trade_type", WXPayConstants.TRADE_TYPE_JSAPI);
        data.put("openid", openid);
        data.put("timeStamp", "" + new Date().getTime()/1000);
//        data.put("product_id", "12");  //TODO get from course.id?
        // data.put("time_expire", "20170112104120");

        boolean successful = false;
        JSONObject result = new JSONObject();
        try {
//            WXPay wxpay = new WXPay(WXPayConfigImpl.getInstance(), WXPayConstants.SignType.MD5);
            Map<String, String> unifiedOrderResult = WXPay.getInstance().unifiedOrder(data);
            logger.info("unfied order result： " + unifiedOrderResult);

            String return_code = unifiedOrderResult.get("return_code");
            if("SUCCESS".equals(return_code)){
                String result_code = unifiedOrderResult.get("result_code");
                if("SUCCESS".equals(result_code)) {
                    successful = true;
                }
            }

            result.put("status","0");
            JSONObject resultData = new JSONObject();
            if (successful)
            {
                resultData.put("nonceStr", unifiedOrderResult.get("nonce_str_req"));
                resultData.put("prepayId", unifiedOrderResult.get("prepay_id"));
                resultData.put("signType", WXPayConstants.SignType.MD5);
                resultData.put("timestamp", unifiedOrderResult.get("timestamp"));

                Map<String, String> paySignData = new HashMap<String, String>();
                paySignData.put("appId", WXPayConfigImpl.getInstance().getAppID());
                paySignData.put("nonceStr", unifiedOrderResult.get("nonce_str_req"));
                paySignData.put("package", "prepay_id=" + unifiedOrderResult.get("prepay_id"));
                paySignData.put("signType", WXPayConstants.SignType.MD5.toString());
                paySignData.put("timeStamp", unifiedOrderResult.get("timestamp"));
                String paySign = WXPayUtil.generateSignature(paySignData, WXPayConfigImpl.getInstance().getKey(), WXPayConstants.SignType.MD5);
                resultData.put("paySign", paySign);

                result.put("message","成功");
            }
            else
            {
                result.put("message", unifiedOrderResult.get("return_msg"));
            }
            result.put("data", resultData);

        } catch (Exception e) {
            logger.info("prepay_id failed with exception: " + e.getMessage());
        }

        HttpClientUtil.saveOutTradeNo(request, outTradeNo);

        return result;
    }

    @RequestMapping("/H5PrePay")
    public @ResponseBody Object H5PrePay(HttpServletRequest request, HttpServletResponse response)throws Exception{
//        String openid = request.getParameter("openid");
//        String fee = request.getParameter("fee");
        String courseId = request.getParameter("courseId");
        String memebersCount = request.getParameter("membersCount");
        String param = request.getParameter("param");

        logger.info("prepay_id with  courseId {}, membersCount {}", courseId, memebersCount);

        long courseIdLong = Long.parseLong(courseId);
        BigDecimal price = gpConfig.getPrice(courseIdLong, Integer.parseInt(memebersCount));
        if (price == null)
        {
            logger.error("No group purchase price available for courseId " + courseId + " and member count " + memebersCount);
            return EnumUtil.errorToJson(ErrorCode.GROUP_PURCHASE_MEMBER_CONFIG_NOT_AVAILABLE);
        }

        Course course = courseService.findCourseById(courseIdLong);
        if (course == null) {
            logger.error("Find course for courseId:{} error, no such course", courseIdLong);
            return EnumUtil.errorToJson(ErrorCode.NO_SUCH_COURSE);
        }

        int fee = price.multiply(new BigDecimal(course.getSessionCount() * 100)).intValue();
        logger.info("prepay_id with fee {}, unit is fen", fee);
//        String outTradeNo = WXPayUtil.generateOutTradeNo();
        HashMap<String, String> data = new HashMap<String, String>();
//        if("12".equals(courseId)){
            data.put("body", "团购金额"); //TODO get from course.title?
//        }else {
//            data.put("body", "名校家长圈团购"); //TODO get from course.title?
//        }
        //用前台的随机数来当以交易号
        data.put("out_trade_no", param);
        data.put("device_info", "WEB");
        data.put("fee_type", "CNY");
        data.put("total_fee", "" + fee);  //TODO get from course.price?  *100 to convert to 分？
        data.put("spbill_create_ip",request.getRemoteAddr());
        data.put("scene_info", "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"http://mxjzq.com/\",\"wap_name\": \"参团支付\"}}");
        data.put("notify_url", WXPayConstants.NOTIFY_URL);
        data.put("trade_type", WXPayConstants.TRADE_TYPE_MWEB);
        data.put("appid", WXPay.getInstance().getAppID());
        data.put("mch_id", WXPay.getInstance().getMchID());
        data.put("nonce_str", WXPayUtil.generateUUID());
        data.put("sign_type", WXPayConstants.HMACSHA256);

        data.put("sign", WXPayUtil.generateSignature(data,  WXPay.getInstance().getKey(), WXPayConstants.SignType.HMACSHA256));
//        String xml = WXPayUtil.mapToXml(data);
//        Map<String,String> headers = new HashMap<>();
//        headers.put("Content-Type","test/xml;charset=UTF-8");
//        Map<String,String> body = new HashMap<>();
//        body.put("xml",xml);

        boolean successful = false;
        JSONObject result = new JSONObject();

        try {
            Map<String, String> unifiedOrderResult = WXPay.getInstance().unifiedOrder(data);
            logger.info("unfied order result： " + unifiedOrderResult);

            String return_code = unifiedOrderResult.get("return_code");
            if("SUCCESS".equals(return_code)){
                String result_code = unifiedOrderResult.get("result_code");
                if("SUCCESS".equals(result_code)) {
                    successful = true;
                }
            }

            result.put("status","0");
            JSONObject resultData = new JSONObject();
            if (successful)
            {
                String url = unifiedOrderResult.get("mweb_url").toString()+"&redirect_url="+
                        URLEncoder.encode("http://www.mxjzq.com/H5/?state="+courseId+"&H5=1");//获得统一下单接口返回的链接
                HttpClientUtil.saveOutTradeNo(request, param);
                logger.info("Get H5 pay url : " + url);
                  return url;
            }
            else
            {
                return "false";
            }
        } catch (Exception e) {
            logger.info("prepay_id failed with exception: " + e.getMessage());
        }

        return  "false";
    }

    @RequestMapping("/notify")
    public String notify(HttpServletRequest request, HttpServletResponse response) {
        logger.info("In notify, notified by Tencent");
        Enumeration<String> em = request.getParameterNames();
        StringBuilder requestParams = new StringBuilder();
        while (em.hasMoreElements()) {
            String name = em.nextElement();
            String value = request.getParameter(name);
            requestParams.append(requestParams.length() == 0 ? "?" : "&").append(name).append("=").append(value);
        }

        logger.info("In notify, requestParams: " + requestParams);

        String notifyResponse = getWeChatPayReturn(request);//getWeChatPayReturnTest(request);

        logger.info("In notify, notifyResponse: " + notifyResponse);

        return notifyResponse;
    }

    public String getWeChatPayReturn(HttpServletRequest request){
        try {
            InputStream inStream = request.getInputStream();
            int _buffer_size = 1024;
            if (inStream != null) {
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] tempBytes = new byte[_buffer_size];
                int count = -1;
                while ((count = inStream.read(tempBytes, 0, _buffer_size)) != -1) {
                    outStream.write(tempBytes, 0, count);
                }
                tempBytes = null;
                outStream.flush();
                //将流转换成字符串
                String result = new String(outStream.toByteArray(), "UTF-8");
                logger.info("In getWeChatPayReturn, result: {}", result);
                //将字符串解析成XML
//                Document doc = DocumentHelper.parseText(result);
                //将XML格式转化成MAP格式数据
                Map<String, String> resultMap = WXPayUtil.xmlToMap(result);
                logger.info("In getWeChatPayReturn, resultMap: {}", resultMap);
                //后续具体自己实现

                GroupPurchasePayment gpPayment = new GroupPurchasePayment();
                gpPayment.openid(resultMap.get("openid")).transaction_id(resultMap.get("transaction_id"))
                        .out_trade_no(resultMap.get("out_trade_no")).cash_fee(resultMap.get("cash_fee"))
                        .total_fee(resultMap.get("total_fee")).fee_type(resultMap.get("fee_type"))
                        .trade_type(resultMap.get("trade_type")).device_info(resultMap.get("device_info"))
                        .time_end(resultMap.get("time_end")).is_subscribe(resultMap.get("is_subscribe"))
                        .bank_type(resultMap.get("bank_type"));

                logger.info("In getWeChatPayReturn, adding payment: {}", gpPayment);
                this.courseMapper.addGroupPurchasePayment(gpPayment);
                //通知微信支付系统接收到信息
                return "<xml><return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>"
                        + "</xml>";

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.info("In getWeChatPayReturn, exception: {}", e.getMessage());
        }
        //如果失败返回错误，微信会再次发送支付信息
        return "fail";
    }

    public String getWeChatPayReturnTest(HttpServletRequest request){
        try {
            if (true) {
                Map<String, String> resultMap = new HashMap<String, String>();
                resultMap.put("openid", "oz2uQ0-7SkoqBGruXb5jGHqyfX1E");
                resultMap.put("transaction_id", "4200000019201801048197873561");
                resultMap.put("out_trade_no", "20180104211203de73cb13bfc54236a2");
                resultMap.put("cash_fee", "1");
                resultMap.put("total_fee", "12");
                resultMap.put("fee_type", "CNY");
                resultMap.put("trade_type", "JSAPI");
                resultMap.put("device_info", "WEB");
                resultMap.put("time_end", "20180104211212");
                resultMap.put("is_subscribe", "Y");
                resultMap.put("bank_type", "SPDB_CREDIT");

                logger.info("In getWeChatPayReturn, resultMap: {}", resultMap);
                //后续具体自己实现

                GroupPurchasePayment gpPayment = new GroupPurchasePayment();
                gpPayment.openid(resultMap.get("openid")).transaction_id(resultMap.get("transaction_id"))
                        .out_trade_no(resultMap.get("out_trade_no")).cash_fee(resultMap.get("cash_fee"))
                        .total_fee(resultMap.get("total_fee")).fee_type(resultMap.get("fee_type"))
                        .trade_type(resultMap.get("trade_type")).device_info(resultMap.get("device_info"))
                        .time_end(resultMap.get("time_end")).is_subscribe(resultMap.get("is_subscribe"))
                        .bank_type(resultMap.get("bank_type"));

                logger.info("In getWeChatPayReturn, adding payment: {}", gpPayment);
                this.courseMapper.addGroupPurchasePayment(gpPayment);

                //通知微信支付系统接收到信息
                return "<xml><return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>"
                        + "</xml>";

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.info("In getWeChatPayReturn, exception: {}", e.getMessage());
        }
        //如果失败返回错误，微信会再次发送支付信息
        return "fail";
    }

    @RequestMapping("/refund1")
    // oz2uQ0-7SkoqBGruXb5jGHqyfX1E
    public @ResponseBody Object refund1(HttpServletRequest request, HttpServletResponse response){
//        String openid = request.getParameter("openid");
        String transactionId = request.getParameter("transactionId");
        String fee = request.getParameter("fee");

        logger.info("refund with openid {} and fee {}", transactionId, fee);

//        Map<String, String> refundResult = WXPayUtil.refund(transactionId, fee);
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("transaction_id", transactionId);//"4200000055201801101745313698"); //TODO get from gp
        data.put("out_refund_no", WXPayUtil.generateOutTradeNo());
        data.put("total_fee", fee);  //TODO get from gp?
        data.put("refund_fee", fee);  //TODO get from gp?
        data.put("refund_fee_type", "CNY");
        data.put("refund_desc", "名校家长圈团购解散");

        boolean successful = false;
        JSONObject result = new JSONObject();
        try {
//            WXPay wxpay = new WXPay(WXPayConfigImpl.getInstance(), WXPayConstants.SignType.MD5);
            Map<String, String> refundResult = WXPay.getInstance().refund(data);
            logger.info("refund result： " + refundResult);

            String return_code = refundResult.get("return_code");
            if("SUCCESS".equals(return_code)){
                String result_code = refundResult.get("result_code");
                if("SUCCESS".equals(result_code)) {
                    successful = true;
                }
            }

            if (successful)
            {
                GroupPurchaseRefund refund = new GroupPurchaseRefund();
                refund.transaction_id(refundResult.get("transaction_id"))
                        .out_refund_no(refundResult.get("out_refund_no")).refund_id(refundResult.get("refund_id"))
                        .total_fee(Double.parseDouble(refundResult.get("total_fee")))
                        .cash_fee(Double.parseDouble(refundResult.get("cash_fee")))
                        .refund_fee(Double.parseDouble(refundResult.get("refund_fee")))
                        .cash_refund_fee(Double.parseDouble(refundResult.get("cash_refund_fee")))
                        .coupon_refund_fee(Double.parseDouble(refundResult.get("coupon_refund_fee")))
                        .coupon_refund_count(Double.parseDouble(refundResult.get("coupon_refund_count")))
                        .refund_channel(refundResult.get("refund_channel"));
                if (this.courseMapper.addGroupPurchaseRefund(refund) != 1)
                {
                    logger.error("Failed to persist refund result: " + refund);
                }
                else
                {
                    logger.info("Persisted refund result: " + refund);
                }
            }

            result.put("status","0");
            result.put("message","成功");
            result.put("data", refundResult);

        } catch (Exception e) {
            logger.info("refund failed with exception: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    @RequestMapping("/refund")
    public @ResponseBody Object refund(HttpServletRequest request, HttpServletResponse response){
        String transactionId = request.getParameter("transactionId");
        String fee = request.getParameter("fee");

        logger.info("refund with transactionId {} and fee {}", transactionId, fee);

//        Map<String, String> refundResult = WXPayUtil.refund(transactionId, fee);
//        WXPayUtil.getInstance().persistRefundResult(refundResult);

//        Map<String, String> refundResult = Refunder.refund(transactionId, fee);
        GroupPurchaseRefund gpRefund = new GroupPurchaseRefund();
        gpRefund.transaction_id(transactionId).cash_fee(Double.parseDouble(fee));
        Map<String, String> refundResult = refunder.doRefund(gpRefund);

        JSONObject result = new JSONObject();
        result.put("status","0");
        result.put("message","成功");
        result.put("data", refundResult);

        return result;
    }

    @RequestMapping("/queryrefundcandidate")
    public @ResponseBody Object queryrefundcandidate(HttpServletRequest request, HttpServletResponse response){
        List<GroupPurchaseRefund> candidates = this.courseMapper.findGroupPurchaseRefundCandidates();

        JSONObject result = new JSONObject();
        result.put("status","0");
        result.put("message","成功");
        result.put("data", candidates);

        return result;
    }

    @RequestMapping("/queryrefund")
    public @ResponseBody Object queryrefund(HttpServletRequest request, HttpServletResponse response){
        List<GroupPurchaseRefund> refunds = this.courseMapper.findGroupPurchaseRefund();

        JSONObject result = new JSONObject();
        result.put("status","0");
        result.put("message","成功");
        result.put("data", refunds);

        return result;
    }

    @RequestMapping("/refundquery")
    // oz2uQ0-7SkoqBGruXb5jGHqyfX1E
    public @ResponseBody Object refundQuery(HttpServletRequest request, HttpServletResponse response){
//        String openid = request.getParameter("openid");
        String transactionId = request.getParameter("transactionId");
//        String fee = request.getParameter("fee");

        logger.info("refundquery with openid {}", transactionId);

        HashMap<String, String> data = new HashMap<String, String>();
        data.put("transaction_id", transactionId);//"4200000055201801101745313698"); //TODO get from gp
        // 4200000023201801079685406167

//        boolean successful = false;
        JSONObject result = new JSONObject();
        try {
//            WXPay wxpay = new WXPay(WXPayConfigImpl.getInstance(), WXPayConstants.SignType.MD5);
            Map<String, String> refundQueryResult = WXPay.getInstance().refundQuery(data);
            logger.info("refund query result： " + refundQueryResult);

            boolean successful = false;
            String return_code = refundQueryResult.get("return_code");
            if("SUCCESS".equals(return_code)){
                String result_code = refundQueryResult.get("result_code");
                if("SUCCESS".equals(result_code)) {
                    successful = true;
                }
            }

            result.put("status","0");
            result.put("message","成功");
            result.put("data", refundQueryResult);

        } catch (Exception e) {
            logger.info("refundquery failed with exception: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }


//    @RequestMapping(value = "/code4learning", method = RequestMethod.GET)
//    public @ResponseBody Object code4learning(HttpServletRequest request, HttpServletResponse response){
//        String code = request.getParameter("code");
//        String state = request.getParameter("state"); // TODO parse state for couse id etc
//        //TODO authroize course by state (8_1) 8 is course id
//        logger.info("code4learning with code: " + code + ", and state: " + state);// + ", and url: " + url);
//
////        AuthToken token = HttpClientUtil.getAuthToken(wechatConfig.getAppid(), wechatConfig.getAppsecret(), code);
//        AuthToken token = HttpClientUtil.getAuthToken(request, code);
//
//        String unionid = token.getUnionid();
//        logger.info("token is {} and unionid is {}.", token, unionid);
//
//        // no need to call getUserInfo as AuthToken already includes union id
////        UserInfo userInfo = HttpClientUtil.getUserInfo(token.getAccess_token(), token.getOpenid());
////
////        logger.info("userInfo is {}.", userInfo);
////
////        String unionid = userInfo.getUnionid();
//
//        String authorizeResult = authorizeAccess(unionid, state);
//        if (authorizeResult != null)
//        {
//            return authorizeResult;
//        }
//
//        String uri = "/storyline-test/story_html5.html"; // TODO, forward to correct course
//        logger.info("In elearning, fwd to uri: {}", uri);
//        try
//        {
//            request.getRequestDispatcher(uri).forward(request,response);
//            logger.info("In elearning, dispatcher forwarded");
//            return "";
//        }
//        catch (Exception exception)
//        {
//            logger.info("In elearning, dispatcher forwarded exception: {}." + exception.getMessage());
//            exception.printStackTrace();
//        }
//
//        logger.info("In elearning, dispatcher forwarded done, return");
//        return "";
//    }

    @RequestMapping(value = "/code4learning", method = RequestMethod.GET)
    public @ResponseBody Object code4learning(HttpServletRequest request, HttpServletResponse response){
        String code = request.getParameter("code");
        String state = request.getParameter("state"); // TODO parse state for couse id etc
        //TODO authroize course by state (8_1) 8 is course id, 1 is channel
        logger.info("code4learning with code: " + code + ", and state: " + state);// + ", and url: " + url);

        String[] stateParse = HttpClientUtil.parseState(state);
        String courseId = stateParse != null ? stateParse[0] : null;
        String channel = stateParse != null && stateParse.length >= 2 ? stateParse[1] : null;

        logger.info("In code4learning, parsed courseId {} and channel {}, check course access", courseId, channel);
        AuthToken token = HttpClientUtil.getAuthToken(request, code, channel);

        String unionid = token.getUnionid();
        logger.info("token is {} and unionid is {}.", token, unionid);

        boolean sessionAuthorized = isSessionAuthorized(request, courseId);

        if (sessionAuthorized)
        {
            logger.info("retrieved session authorized from user {} session for course{}, no need to authorize.", unionid, courseId);
        }
        else
        {
            String authorizeResult = authorizeAccess(request, unionid, courseId);
            if (authorizeResult != null)
            {
                logger.info("code4learning.authorizeResuResult: {}", authorizeResult);            }
        }

        String uri = "/courses/" + courseId + "/sessions.html"; //story_html5.html"; // TODO, forward to correct course
//        String uri = "/courses/" + courseId + "/" + 1 + "/story_html5.html";

        logger.info("In elearning, fwd to uri: {}", uri);
        try
        {
            request.getRequestDispatcher(uri).forward(request,response);
            logger.info("In elearning, dispatcher forwarded");
            return "";
        }
        catch (Exception exception)
        {
            logger.info("In elearning, dispatcher forwarded exception: {}." + exception.getMessage());
            exception.printStackTrace();
        }

        logger.info("In elearning, dispatcher forwarded done, return");
        return "";
    }

    @RequestMapping(value = "**/learning", method = RequestMethod.GET)
    public @ResponseBody Object learning(HttpServletRequest request, HttpServletResponse response){
        String[] paths = request.getServletPath().split("/");
        String courseId = paths[2];
        String sessionId = paths[3];
        boolean sessionAuthorized = isSessionAuthorized(request, courseId);

        if (sessionAuthorized)
        {
            logger.info("In learning, retrieved session authorized from user {} session for course{}, no need to authorize.", courseId);
        }
        else
        {
            return "无权访问或会话过期， 请重新登陆！";
        }

        String uri = "/courses/" + courseId + "/" + sessionId + "/story_html5.html";
        logger.info("In learning, fwd to uri: {}", uri);
        try
        {
            request.getRequestDispatcher(uri).forward(request,response);
            logger.info("In elearning, dispatcher forwarded");
            return "";
        }
        catch (Exception exception)
        {
            logger.info("In elearning, dispatcher forwarded exception: {}." + exception.getMessage());
            exception.printStackTrace();
        }

        logger.info("In elearning, dispatcher forwarded done, return");
        return "";
    }

    @RequestMapping(value = "**/testlearning", method = RequestMethod.GET)
    public @ResponseBody Object testlearning(HttpServletRequest request, HttpServletResponse response){
        logger.info("In learning, request URL {}, request URI {}, request context path {}, servelet path {}, query {}.",
                request.getRequestURL(),
                request.getRequestURI(),
                request.getContextPath(),
                request.getServletPath(),
                request.getQueryString());

        String ids = request.getParameter("id");
        String[] idArray = ids.split("_");
        String courseId = idArray[0];
        String sessionId = idArray[1];

        //request URL http://localhost:8080/mingxiao/user/123/1/testlearning,
        //request URI /mingxiao/user/123/1/testlearning,
        // request context path /mingxiao,
        // servelet path /user/123/1/testlearning.
        String[] paths = request.getServletPath().split("/");
        courseId = paths[2];
        sessionId = paths[3];

        boolean sessionAuthorized = true; //isSessionAuthorized(request, courseId);

        if (sessionAuthorized)
        {
            logger.info("In learning, retrieved session authorized from user {} session for course{}, no need to authorize.", courseId);
        }
        else
        {
            return "无权访问或会话过期， 请重新登陆！";
        }

        String uri = "/courses/" + courseId + "/" + sessionId + "/story_html5.html";
        logger.info("In learning, fwd to uri: {}", uri);
        try
        {
            request.getRequestDispatcher(uri).forward(request,response);
            logger.info("In elearning, dispatcher forwarded");
            return "";
        }
        catch (Exception exception)
        {
            logger.info("In elearning, dispatcher forwarded exception: {}." + exception.getMessage());
            exception.printStackTrace();
        }

        logger.info("In elearning, dispatcher forwarded done, return");
        return "";
    }

    @RequestMapping(value = "**/deny/**", method = RequestMethod.GET)
    public @ResponseBody Object deny(HttpServletRequest request, HttpServletResponse response){
        logger.info("In deny, request URL {}, request URI {}, request context path {}, servelet path {}, query {}.");
        return "";
    }

    public String authorizeAccess(HttpServletRequest request, String unionid, String passedCourseId)
    {
        logger.info("In authorizeAccess, check access of course {} for user {}", passedCourseId, unionid);
        if (passedCourseId == null || passedCourseId.isEmpty())
        {
            return "没有课程信息！";
        }

        if (courseMapper.findAdminMember(unionid) == 0)
        {
            List<GroupPurchase> gps = courseMapper.findGroupPurchaseByUnionidAndCourseId(unionid,passedCourseId);
            logger.info("In authorizeAccess, gps: {}", gps);
            if (gps == null || gps.size() == 0)
            {
                return "您尚未购买该课程！";
            }
            //TODO uncomment below else once 公众号菜单 url 换成正式 "state=courseId_xxxx"
//            else
//            {
//                boolean foundGp = false;
//                String tmpResult = null;
//                for (GroupPurchase gp: gps)
//                {
//                    long courseId = gp.getCourseId();
//                    // Note, we will control access from course level, not from group purchase level:
//                    // user may have multiple group purchase for one course, like 4_6, 4_12 meaning course 4 with 6 members and 12 members
//                    // if one of 6 members or 12 members group purchase is completed, let user access, otherwise deny access
//
//                    if (passedCourseId.equals(String.valueOf(courseId)))
//                    {
//                        logger.info("In authorizeAccess, found courseId {} from group purchase {}", passedCourseId, gp.getId());
//                        foundGp = true;
//                        if (gp.getStatus() != CourseService.GROUP_PURCHASE_COMPLETED)
//                        {
//                            // continue to see if user's other group purchase is completed
//                            logger.info("In authorizeAccess, found group purchase {} not completed, continue to find other group purchase", gp.getId());
//                            tmpResult = "您的该课程拼团尚未成团！";
//                            continue;
//                        }
//
//                        Course course = courseService.findCourseById(courseId);
//                        logger.info("In authorizeAccess, course {} details is {}", courseId, course);
//                        if (course.getUnitType() != 0)
//                        {
//                            if (course.getUnitType() == CourseService.UNIT_TYPE_MONTH)
//                            {
//                                Calendar calendar = Calendar.getInstance();
//                                calendar.setTimeInMillis(gp.getCompleted().getTime());
//                                logger.info("In authorizeAccess, {} complted at {}", courseId, calendar.getTime());
//                                calendar.add(Calendar.MONTH, course.getSessionCount());
//                                logger.info("In authorizeAccess, {} expired at {}", courseId, calendar.getTime());
//                                long expiredTime = calendar.getTimeInMillis();
//                                long currentTime = System.currentTimeMillis();
//                                logger.info("In authorizeAccess, currentTime {}", new Date(currentTime));
//                                if (currentTime > expiredTime)
//                                {
//                                    String result = "您的该课程拼团" + "(" + course.getSessionCount() + course.getUnitStr() + ")"
//                                            + "成立于" + gp.getCompleted() + ", 已于" + new Timestamp(expiredTime) + "过期！";
//
//                                    logger.info("In authorizeAccess, result is {}", result);
//                                    return result;
//                                }
//                            }
//                        }
//
//                        // here we have passed the authorization, now clear tmpResult if any
//                        tmpResult = null;
//                        break;
//                    }
//                }
//
//                if (tmpResult != null)
//                {
//                    // in this case, the user's all group purchase are not completed. deny the access
//                    return tmpResult;
//                }
//
//                if (!foundGp)
//                {
//                    logger.info("In authorizeAccess, didn't find courseId {}", passedCourseId);
//                    return "您尚未购买该课程！";
//                }
//            }

            logger.info("In authorizeAccess, {} passed authorization for course {} access.", unionid, passedCourseId);
        }
        else
        {
            logger.info("In authorizeAccess, {} is admin member.", unionid);
        }

        if (request != null)
        {
            HttpSession session = request.getSession();
            session.setAttribute(passedCourseId + "_access", true);
        }

        return null;
    }

    private boolean isSessionAuthorized(HttpServletRequest request, String courseId)
    {
        HttpSession session = request.getSession();
        Object sessionObj = session.getAttribute(courseId + "_access");
        boolean sessionAuthorized = false;
        if (sessionObj == null)
        {
            logger.info("sessionObj is null.");
            sessionAuthorized = false;
        }
        else
        {
            sessionAuthorized = (Boolean)sessionObj;
        }

        logger.info("retrieved session authorized {} from session for course{}.", sessionAuthorized, courseId);
        return sessionAuthorized;
    }
}
