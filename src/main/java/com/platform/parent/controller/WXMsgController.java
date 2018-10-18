package com.platform.parent.controller;

import com.alibaba.fastjson.JSONObject;
import com.platform.parent.mybatis.bean.GroupPurchase;
import com.platform.parent.mybatis.bean.GroupPurchaseMember;
import com.platform.parent.util.ClientCrendentialToken;
import com.platform.parent.util.HttpClientUtil;
import com.platform.parent.util.WechatUtil;
import com.platform.parent.wxpay.WXPayConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * Created by dengb.
 */
@RestController
@RequestMapping(value = "/message")
public class WXMsgController {
    private static final Logger logger = LoggerFactory.getLogger(WXMsgController.class);

    @RequestMapping(value = "/industry", method = RequestMethod.GET)
    public @ResponseBody
    Object industry() {
        logger.info("industry");
        String url = WXPayConstants.INDUSTRY_GET_URL + ClientCrendentialToken.getToken();
        String queryResult = WechatUtil.wechatApiGet(url);

        JSONObject result = new JSONObject();
        result.put("status","0");
        result.put("message","成功");
        result.put("industry", queryResult);
        return result;
    }


    @RequestMapping(value = "/template", method = RequestMethod.GET)
    public @ResponseBody
    Object template() {
        logger.info("template");
        String url = WXPayConstants.TEMPLATE_GET_URL + ClientCrendentialToken.getToken();
        String queryResult = WechatUtil.wechatApiGet(url);

        JSONObject result = new JSONObject();
        result.put("status","0");
        result.put("message","成功");
        result.put("template", queryResult);
        return result;
    }

    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public @ResponseBody
    Object send(@RequestParam(required = false, value = "courseId") String courseId,
                @RequestParam(required = false, value = "openId") String openId,
                @RequestParam(required = false, value = "templateId") String templateId,
                @RequestParam(required = false, value = "channel") String channel) {
        logger.info("send");
        String url = WXPayConstants.MESSAGE_SEND_URL + ClientCrendentialToken.getToken();
        GroupPurchase gp = new GroupPurchase();
        gp.courseId(Long.parseLong(courseId));

        GroupPurchaseMember gpm = new GroupPurchaseMember();
        gpm.openid(openId);
        String queryResult = WechatUtil.wechatApiGet(url, HttpClientUtil.getRequestBody(HttpClientUtil.getRequestMap(gp, gpm, templateId, channel)));

        JSONObject result = new JSONObject();
        result.put("status","0");
        result.put("message","成功");
        result.put("template", queryResult);
        return result;
    }
}
