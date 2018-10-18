package com.platform.parent.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by dengb.
 */
@Component
@ConfigurationProperties
@PropertySource("classpath:config/wechat.properties")
public class WechatConfig {
    private String server, domainpath, appid, appsecret, server_token_url, refund_interval, refund_expiration_days;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getDomainpath() {
        return domainpath;
    }

    public void setDomainpath(String domainpath) {
        this.domainpath = domainpath;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }

    public String getServer_token_url() {
        return server_token_url;
    }

    public void setServer_token_url(String server_token_url) {
        this.server_token_url = server_token_url;
    }

    public String getRefundInterval() {
        return refund_interval;
    }

    public void setRefundInterval(String refund_interval) {
        this.refund_interval = refund_interval;
    }

    public String getRefundExpirationDays() {
        return refund_expiration_days;
    }

    public void setRefundExpirationDays(String refund_expiration_days) {
        this.refund_expiration_days = refund_expiration_days;
    }
}