package com.platform.parent.mybatis.bean;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;

/**
 * Created by dengb.
 */
public class GroupPurchaseMember{
    private static final Logger logger = LoggerFactory.getLogger(GroupPurchaseMember.class);

    private long id, groupId;
    private String unionid;
    private String openid;
    private String nickname;
    private String headimgurl;
    private Timestamp joined;
    private String channel;
    //  payment info
    private String outTradeNo;

    public GroupPurchaseMember id(long id) {
        this.id = id;
        return this;
    }

    public GroupPurchaseMember groupId(long groupId) {
        this.groupId = groupId;
        return this;
    }

    public GroupPurchaseMember unionid(String unionid) {
        this.unionid = unionid;
        return this;
    }

    public GroupPurchaseMember openid(String openid) {
        this.openid = openid;
        return this;
    }

    public GroupPurchaseMember nickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public GroupPurchaseMember headimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
        return this;
    }

    public GroupPurchaseMember joined(Timestamp joined)
    {
        this.joined = joined;
        return this;
    }

    public GroupPurchaseMember channel(String channel)
    {
        this.channel = channel;
        return this;
    }

    public GroupPurchaseMember outTradeNo(String outTradeNo)
    {
        this.outTradeNo = outTradeNo;
        return this;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public Timestamp getJoined() {
        return joined;
    }

    public void setJoined(Timestamp joined) {
        this.joined = joined;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    @Override
    public String toString()
    {
        return "id: " + id + ", groupId: " + groupId  + ", unionid: " + unionid  + ", openid: " + openid
                + ", nickname: " + nickname  + ", headimgurl: " + headimgurl + ", joined: " + joined
                + ", channel: " + channel + ", outTradeNo: " + outTradeNo;
    }
}