package com.platform.parent.mybatis.bean;

import java.sql.Timestamp;

public class GroupPurchaseRefund {
    private long id;

    private String transaction_id, out_refund_no, out_trade_no, refund_id,
            refund_channel;

    private double total_fee, cash_fee, refund_fee, cash_refund_fee, coupon_refund_fee, coupon_refund_count;

    private int expired_days;

    private Timestamp refund_time;


    public GroupPurchaseRefund id(long id) {
        this.id = id;
        return this;
    }

    public GroupPurchaseRefund transaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
        return this;
    }

    public GroupPurchaseRefund out_refund_no(String out_refund_no) {
        this.out_refund_no = out_refund_no;
        return this;
    }


    public GroupPurchaseRefund out_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
        return this;
    }

    public GroupPurchaseRefund refund_id(String refund_id) {
        this.refund_id = refund_id;
        return this;
    }

    public GroupPurchaseRefund refund_channel(String refund_channel) {
        this.refund_channel = refund_channel;
        return this;
    }

    public GroupPurchaseRefund total_fee(double total_fee) {
        this.total_fee = total_fee;
        return this;
    }

    public GroupPurchaseRefund cash_fee(double cash_fee) {
        this.cash_fee = cash_fee;
        return this;
    }

    public GroupPurchaseRefund refund_fee(double refund_fee) {
        this.refund_fee = refund_fee;
        return this;
    }

    public GroupPurchaseRefund cash_refund_fee(double cash_refund_fee) {
        this.cash_refund_fee = cash_refund_fee;
        return this;
    }

    public GroupPurchaseRefund coupon_refund_fee(double coupon_refund_fee) {
        this.coupon_refund_fee = coupon_refund_fee;
        return this;
    }

    public GroupPurchaseRefund coupon_refund_count(double coupon_refund_count) {
        this.coupon_refund_count = coupon_refund_count;
        return this;
    }

    public GroupPurchaseRefund expired_days(int expired_days) {
        this.expired_days = expired_days;
        return this;
    }

    public GroupPurchaseRefund refund_time(Timestamp refund_time) {
        this.refund_time = refund_time;
        return this;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOut_refund_no() {
        return out_refund_no;
    }

    public void setOut_refund_no(String out_refund_no) {
        this.out_refund_no = out_refund_no;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getRefund_id() {
        return refund_id;
    }

    public void setRefund_id(String refund_id) {
        this.refund_id = refund_id;
    }

    public String getRefund_channel() {
        return refund_channel;
    }

    public void setRefund_channel(String refund_channel) {
        this.refund_channel = refund_channel;
    }

    public double getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(double total_fee) {
        this.total_fee = total_fee;
    }

    public double getCash_fee() {
        return cash_fee;
    }

    public void setCash_fee(double cash_fee) {
        this.cash_fee = cash_fee;
    }

    public double getRefund_fee() {
        return refund_fee;
    }

    public void setRefund_fee(double refund_fee) {
        this.refund_fee = refund_fee;
    }

    public double getCash_refund_fee() {
        return cash_refund_fee;
    }

    public void setCash_refund_fee(double cash_refund_fee) {
        this.cash_refund_fee = cash_refund_fee;
    }

    public double getCoupon_refund_fee() {
        return coupon_refund_fee;
    }

    public void setCoupon_refund_fee(double coupon_refund_fee) {
        this.coupon_refund_fee = coupon_refund_fee;
    }

    public double getCoupon_refund_count() {
        return coupon_refund_count;
    }

    public void setCoupon_refund_count(double coupon_refund_count) {
        this.coupon_refund_count = coupon_refund_count;
    }

    public int getExpired_days() {
        return expired_days;
    }

    public void setExpired_days(int expired_days) {
        this.expired_days = expired_days;
    }

    public Timestamp getRefund_time() {
        return refund_time;
    }

    public void setRefund_time(Timestamp refund_time) {
        this.refund_time = refund_time;
    }

    @Override
    public String toString()
    {
        return "id: " + id + ", transaction_id: " + transaction_id + ", out_refund_no: " + out_refund_no +
                ", out_trade_no: " + out_trade_no + ", refund_id: " + refund_id + ", refund_channel: " + refund_channel +
                ", total_fee: " + total_fee + ", cash_fee: " + cash_fee + ", refund_fee: " + refund_fee +
                ", cash_refund_fee: " + cash_refund_fee + ", coupon_refund_fee: " + coupon_refund_fee +
                ", coupon_refund_count: " + coupon_refund_count + ", expired_days: " + expired_days +
                ", refund_time: " + refund_time;
    }
}
