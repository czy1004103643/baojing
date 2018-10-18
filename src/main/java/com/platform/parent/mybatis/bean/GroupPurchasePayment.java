package com.platform.parent.mybatis.bean;

public class GroupPurchasePayment {
    private long id;

    private String openid, transaction_id, out_trade_no, cash_fee,
            total_fee, fee_type, trade_type, device_info, time_end, is_subscribe, bank_type;

    public GroupPurchasePayment id(long id) {
        this.id = id;
        return this;
    }

    public GroupPurchasePayment openid(String openid) {
        this.openid = openid;
        return this;
    }

    public GroupPurchasePayment transaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
        return this;
    }

    public GroupPurchasePayment out_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
        return this;
    }

    public GroupPurchasePayment cash_fee(String cash_fee) {
        this.cash_fee = cash_fee;
        return this;
    }

    public GroupPurchasePayment total_fee(String total_fee) {
        this.total_fee = total_fee;
        return this;
    }

    public GroupPurchasePayment fee_type(String fee_type) {
        this.fee_type = fee_type;
        return this;
    }

    public GroupPurchasePayment trade_type(String trade_type) {
        this.trade_type = trade_type;
        return this;
    }

    public GroupPurchasePayment device_info(String device_info) {
        this.device_info = device_info;
        return this;
    }

    public GroupPurchasePayment time_end(String time_end) {
        this.time_end = time_end;
        return this;
    }

    public GroupPurchasePayment is_subscribe(String is_subscribe) {
        this.is_subscribe = is_subscribe;
        return this;
    }

    public GroupPurchasePayment bank_type(String bank_type) {
        this.bank_type = bank_type;
        return this;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getCash_fee() {
        return cash_fee;
    }

    public void setCash_fee(String cash_fee) {
        this.cash_fee = cash_fee;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getIs_subscribe() {
        return is_subscribe;
    }

    public void setIs_subscribe(String is_subscribe) {
        this.is_subscribe = is_subscribe;
    }

    public String getBank_type() {
        return bank_type;
    }

    public void setBank_type(String bank_type) {
        this.bank_type = bank_type;
    }

    @Override
    public String toString()
    {
        return "id: " + id + ", openid: " + openid + ", transaction_id: " + transaction_id +
                ", out_trade_no: " + out_trade_no + ", cash_fee: " + cash_fee + ", total_fee: " + total_fee +
                ", fee_type: " + fee_type + ", trade_type: " + trade_type + ", device_info: " + device_info +
                ", time_end: " + time_end + ", is_subscribe: " + is_subscribe + ", bank_type: " + bank_type;

    }
}
