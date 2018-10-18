package com.platform.parent.wxpay;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量
 */
public class WXPayConstants {

    public enum SignType {
        MD5, HMACSHA256
    }

    public static final String DOMAIN_API = "api.mch.weixin.qq.com";
    public static final String DOMAIN_API2 = "api2.mch.weixin.qq.com";
    public static final String DOMAIN_APIHK = "apihk.mch.weixin.qq.com";
    public static final String DOMAIN_APIUS = "apius.mch.weixin.qq.com";


    public static final String FAIL     = "FAIL";
    public static final String SUCCESS  = "SUCCESS";
    public static final String HMACSHA256 = "HMAC-SHA256";
    public static final String MD5 = "MD5";

    public static final String FIELD_SIGN = "sign";
    public static final String FIELD_SIGN_TYPE = "sign_type";

    public static final String MICROPAY_URL_SUFFIX     = "/pay/micropay";
    public static final String UNIFIEDORDER_URL_SUFFIX = "/pay/unifiedorder";
    public static final String ORDERQUERY_URL_SUFFIX   = "/pay/orderquery";
    public static final String REVERSE_URL_SUFFIX      = "/secapi/pay/reverse";
    public static final String CLOSEORDER_URL_SUFFIX   = "/pay/closeorder";
    public static final String REFUND_URL_SUFFIX       = "/secapi/pay/refund";
    public static final String REFUNDQUERY_URL_SUFFIX  = "/pay/refundquery";
    public static final String DOWNLOADBILL_URL_SUFFIX = "/pay/downloadbill";
    public static final String REPORT_URL_SUFFIX       = "/payitil/report";
    public static final String SHORTURL_URL_SUFFIX     = "/tools/shorturl";
    public static final String AUTHCODETOOPENID_URL_SUFFIX = "/tools/authcodetoopenid";

    // sandbox
    public static final String SANDBOX_MICROPAY_URL_SUFFIX     = "/sandboxnew/pay/micropay";
    public static final String SANDBOX_UNIFIEDORDER_URL_SUFFIX = "/sandboxnew/pay/unifiedorder";
    public static final String SANDBOX_ORDERQUERY_URL_SUFFIX   = "/sandboxnew/pay/orderquery";
    public static final String SANDBOX_REVERSE_URL_SUFFIX      = "/sandboxnew/secapi/pay/reverse";
    public static final String SANDBOX_CLOSEORDER_URL_SUFFIX   = "/sandboxnew/pay/closeorder";
    public static final String SANDBOX_REFUND_URL_SUFFIX       = "/sandboxnew/secapi/pay/refund";
    public static final String SANDBOX_REFUNDQUERY_URL_SUFFIX  = "/sandboxnew/pay/refundquery";
    public static final String SANDBOX_DOWNLOADBILL_URL_SUFFIX = "/sandboxnew/pay/downloadbill";
    public static final String SANDBOX_REPORT_URL_SUFFIX       = "/sandboxnew/payitil/report";
    public static final String SANDBOX_SHORTURL_URL_SUFFIX     = "/sandboxnew/tools/shorturl";
    public static final String SANDBOX_AUTHCODETOOPENID_URL_SUFFIX = "/sandboxnew/tools/authcodetoopenid";

    public static final String NOTIFY_URL = "http://www.mxjzq.com/mingxiao/user/notify";
    public static final String TRADE_TYPE_JSAPI = "JSAPI";
    public static final String TRADE_TYPE_MWEB = "MWEB";

    public static final String INDUSTRY_SET_URL = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=";
    public static final String INDUSTRY_GET_URL = "https://api.weixin.qq.com/cgi-bin/template/get_industry?access_token=";

    public static final String TEMPLATE_SET_URL = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=";
    public static final String TEMPLATE_GET_URL = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=";
    public static final String TEMPLATE_DELETE_URL = "https://api.weixin.qq.com/cgi-bin/template/del_private_template?access_token=";

    public static final String MESSAGE_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    public static final String MESSAGE_TEMPLATE_PURCHASE = "tWrflM6VjRl8YpgoUL7radCFlyE2UtMar_qnaJYa9yE";
    public static final String MESSAGE_TEMPLATE_PURCHASE_GROUP = "h26yufp6HkbFxSOuqxn67DTIBRjYpStGQ13xyod7Ljs";
    public static final String MESSAGE_TEMPLATE_PURCHASE_GROUP_1 = "-msPpEikIArgiNWrHge8XSqjSlgd1m1zd55BX343nGU";


    public final static Map<String, String> MESSAGE_TEMPLATE_MAP_PURCHASE = new HashMap<String, String>() {{
        put("name", "value");
        put("remark", "value");
    }};

    public final static Map<String, String> MESSAGE_TEMPLATE_MAP_PURCHASE_GROUP = new HashMap<String, String>() {{
        put("first", "value");
        put("hotelName", "value");
        put("voucher number", "value");
        put("remark", "value");
    }};

    public final static Map<String, String> MESSAGE_TEMPLATE_MAP_PURCHASE__GROUP_1 = new HashMap<String, String>() {{
        put("first", "value");
        put("Pingou_ProductName", "value");
        put("Weixin_ID", "value");
        put("Remark", "value");
    }};

    public final static String GREETINGS = "您好， 团购成功！";


//    {"template":"{\"template_list\":[{\"template_id\":\"qodDflTG_HcHUKtqO3cvUYMRzXz4pHyS1LkZTa0rIsA\",\"title\":
// \"订阅模板消息\",\"primary_industry\":\"\",\"deputy_industry\":\"\",\"content\":\"{{content.DATA}}\",\"example\":\"\"},
// {\"template_id\":\"tWrflM6VjRl8YpgoUL7radCFlyE2UtMar_qnaJYa9yE\",\"title\":\"购买成功通知\",\"primary_industry\":\"IT科技\",
//          \"deputy_industry\":\"互联网|电子商务\",\"content\":\"您好，您已购买成功。\\n\\n商品信息：{{name.DATA}}\\n{{remark.DATA}}\",
//          \"example\":\"您好，您已购买成功。\\n\\n商品信息：微信影城影票\\n有效期：永久有效\\n券号为QQ5024813399，密码为123456890\"},
// {\"template_id\":\"h26yufp6HkbFxSOuqxn67DTIBRjYpStGQ13xyod7Ljs\",\"title\":\"团购成功通知\",\"primary_industry\":\"IT科技\",
//          \"deputy_industry\":\"互联网|电子商务\",\"content\":\"{{first.DATA}}\\n\\n产品名称：{{hotelName.DATA}}\\n团购券号:
//          {{voucher number.DATA}}\\n{{remark.DATA}}\",
//          \"example\":\"您好，团购成功！\\n\\n产品名称：微信饭店\\n团购券号：1013563233771357、 1013563233771358\\n点击查看更多酒店详情；部分酒店已开通网上预约及退款服务。\"},
// {\"template_id\":\"-msPpEikIArgiNWrHge8XSqjSlgd1m1zd55BX343nGU\",\"title\":\"团购结果通知\",\"primary_industry\":\"IT科技\",\
//          "deputy_industry\":\"互联网|电子商务\",\"content\":\"{{first.DATA}}\\n\\n商品名称：{{Pingou_ProductName.DATA}}\\n
//          团长：{{Weixin_ID.DATA}}\\n{{Remark.DATA}}\",
//          \"example\":\"恭喜您，您参加的团购已成功，我们会尽快为您发货。\\n\\n商品名称：我是商品名称\\n团长：我是团长微信名\\n\\n
//          您的已付款项将在3-5天内退至您的支付账户，请留意相关信息。\"}]}","message":"成功","status":"0"}

//    {"touser":"oz2uQ0-7SkoqBGruXb5jGHqyfX1E",
//            "template_id":"tWrflM6VjRl8YpgoUL7radCFlyE2UtMar_qnaJYa9yE",
//            "data":
//        {"name":{"value":"您好，您已购买成功。", "color":"#173177"},
//              "remark":{"value":"微信影城影票\n有效期：永久有效\n券号为QQ5024813399，密码为123456890\n<a href='http://www.baidu.com'>点我</a>","color":"#173177"}}
//    }


//    {"touser":"oz2uQ0-7SkoqBGruXb5jGHqyfX1E",
//            "template_id":"h26yufp6HkbFxSOuqxn67DTIBRjYpStGQ13xyod7Ljs",
//            "data":
//        {"first":{"value":"您好，团购成功！", "color":"#173177"},
//            "hotelName":{"value":"微信饭店","color":"#173177"},
//            "voucher number":{"value":"优惠券 名字","color":"#173177"},
//            "remark":{"value":"点击查看更多酒店详情；部分酒店已开通网上预约及退款服务。","color":"#173177"}}
//    }


//    {"touser":"oz2uQ0-7SkoqBGruXb5jGHqyfX1E",
//            "template_id":"-msPpEikIArgiNWrHge8XSqjSlgd1m1zd55BX343nGU",
//            "data":
//        {"first":{"value":"恭喜您，您参加的团购已成功，我们会尽快为您发货。", "color":"#173177"},
//            "Pingou_ProductName":{"value":"我是商品名称","color":"#173177"},
//            "Weixin_ID":{"value":"我是团长微信名","color":"#173177"},
//            "Remark":{"value":"您的已付款项将在3-5天内退至您的支付账户，请留意相关信息。","color":"#173177"}}
//    }

}

