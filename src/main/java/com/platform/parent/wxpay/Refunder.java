package com.platform.parent.wxpay;

import com.platform.parent.config.WechatConfig;
import com.platform.parent.mybatis.bean.GroupPurchaseRefund;
import com.platform.parent.mybatis.dao.CourseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Refunder {
    private static final Logger logger = LoggerFactory.getLogger(Refunder.class);

    @Autowired
    WechatConfig wechatConfig;

    @Autowired
    CourseMapper courseMapper;

    @Scheduled(cron = "0 0 21 * * MON-FRI")
    public void work() {
        logger.info("Refunder working...");
        refund();
    }

    public void refund()
    {
        List<GroupPurchaseRefund> refundCandidates = courseMapper.findGroupPurchaseRefundCandidates();
        logger.info("Refunder.refund candidates: " + refundCandidates);
        if (refundCandidates != null && refundCandidates.size() > 0)
        {
            for(GroupPurchaseRefund refundCandidate: refundCandidates)
            {
                if (isRefundable(refundCandidate))
                {
                    doRefund(refundCandidate);
                }
            }
        }
    }

    public Map<String, String> doRefund(GroupPurchaseRefund refund)
    {
        if (refund == null)
        {
            return null;
        }

        logger.info("Refund {} has expired for {} days.", refund.getTransaction_id(), refund.getExpired_days());

        String transactionId = refund.getTransaction_id();
        double fee = refund.getCash_fee();
        Map<String, String> refundResult = refund(transactionId, "" + fee);
        persistRefundResult(refundResult);

//        Map<String, String> refundResult = getRefundResult();
//        persistRefundResult(refundResult);

        return refundResult;
    }

    private boolean isRefundable(GroupPurchaseRefund refund)
    {
        logger.info("isRefundable for refund: {}", refund);
        if (refund == null)
        {
            return false;
        }
        String expirationDaysStr = this.wechatConfig.getRefundExpirationDays();
        logger.info("Refund expired {} days and configured expiration days is {}.", refund.getExpired_days(), expirationDaysStr);
        int expirationDays = 0;
        try
        {
            expirationDays = Integer.parseInt(expirationDaysStr);
        }
        catch (NumberFormatException nfe)
        {
            logger.info("Failed to parse configured expiration days {} with exception {}.", expirationDaysStr, nfe.getMessage());
        }

        return refund.getExpired_days() >= expirationDays;
    }

    private static Map<String, String> getRefundResult()
    {
        Map<String, String> result = new HashMap<String, String>();

        result.put("transaction_id", "transaction_id");
        result.put("out_refund_no", "out_refund_no");
        result.put("return_msg", "OK");
        result.put("refund_id", "refund_id");
        result.put("cash_fee", "1");
        result.put("cash_refund_fee", "1");
        result.put("out_trade_no", "out_trade_no");
        result.put("coupon_refund_fee", "0");
        result.put("coupon_refund_count", "0");
        result.put("refund_channel", "");
        result.put("refund_fee", "1");
        result.put("total_fee", "1");
        result.put("result_code", "SUCCESS");
        result.put("return_code", "SUCCESS");

        return result;
    }

    public static Map<String, String> refund(String transactionId, String fee)
    {
        logger.info("refund for transactionId {} and fee {}.", transactionId, fee);
        Map<String, String> refundResult = null;
        if (transactionId == null || fee == null)
        {
            return refundResult;
        }

        int feeInt = -1;
        try
        {
            feeInt = (int)Double.parseDouble(fee);
        }
        catch (NumberFormatException nfe)
        {
            logger.error("Wrong fee passed in: " + nfe.getMessage());
        }

        HashMap<String, String> data = new HashMap<String, String>();
        data.put("transaction_id", transactionId);//"4200000055201801101745313698"); //TODO get from gp
        data.put("out_refund_no", WXPayUtil.generateOutTradeNo());
        data.put("total_fee", "" + feeInt);  //TODO get from gp?

        // deduct transaction fee for refund, if refund fee is less than 1, make it 1
        int refundFee = (int)(feeInt * 0.99);
        refundFee = refundFee > 1 ? refundFee : 1;

        data.put("refund_fee", "" + refundFee);  //TODO get from gp?
        data.put("refund_fee_type", "CNY");
        data.put("refund_desc", "名校家长圈团购解散");

        try {
//            WXPay wxpay = new WXPay(WXPayConfigImpl.getInstance(), WXPayConstants.SignType.MD5);
            refundResult = WXPay.getInstance().refund(data);
        } catch (Exception e) {
            logger.info("refund failed with exception: " + e.getMessage());
            e.printStackTrace();
        }

        logger.info("refund result: " + refundResult);
        return refundResult;
    }

    public int persistRefundResult(Map<String, String> refundResult)
    {
        int result = 0;
        if (refundResult == null || refundResult.isEmpty())
        {
            logger.error("Persist refund result: " + result + " as refundResult is null or empty.");
            return result;
        }

        boolean successful = false;
        String return_code = refundResult.get("return_code");
        if("SUCCESS".equals(return_code)){
            String result_code = refundResult.get("result_code");
            if("SUCCESS".equals(result_code)) {
                successful = true;
            }
        }

        if (successful)
        {
            logger.info("Refund result has succeeded. About to persist refund result");
            GroupPurchaseRefund refund = new GroupPurchaseRefund();
            refund.transaction_id(refundResult.get("transaction_id")).out_refund_no(refundResult.get("out_refund_no"))
                    .out_trade_no(refundResult.get("out_trade_no")).refund_id(refundResult.get("refund_id"))
                    .total_fee(Double.parseDouble(refundResult.get("total_fee")))
                    .cash_fee(Double.parseDouble(refundResult.get("cash_fee")))
                    .refund_fee(Double.parseDouble(refundResult.get("refund_fee")))
                    .cash_refund_fee(Double.parseDouble(refundResult.get("cash_refund_fee")))
                    .coupon_refund_fee(Double.parseDouble(refundResult.get("coupon_refund_fee")))
                    .coupon_refund_count(Double.parseDouble(refundResult.get("coupon_refund_count")))
                    .refund_channel(refundResult.get("refund_channel"));
            result = courseMapper.addGroupPurchaseRefund(refund);
        }
        else
        {
            logger.info("No refund to persist because refund result has failed.");
        }

        logger.info("Persist refund result: " + result);

        return result;
    }
}
