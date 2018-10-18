package com.platform.parent.mybatis.bean;

import java.sql.Timestamp;

public class AdminCandidate {
    private long id;
    private String openid;
    private String unionid;
    private String nickname;
    private String headimgurl;
    private Timestamp created;

    public AdminCandidate id(long id) {
        this.id = id;
        return this;
    }

    public AdminCandidate openid(String openid) {
        this.openid = openid;
        return this;
    }

    public AdminCandidate unionid(String unionid) {
        this.unionid = unionid;
        return this;
    }

    public AdminCandidate nickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public AdminCandidate headimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
        return this;
    }

    public AdminCandidate created(Timestamp created) {
        this.created = created;
        return this;
    }

    private long getId()
    {
        return id;
    }

    private void setId(long id)
    {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
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


    @Override
    public String toString()
    {
        return "id: " + id + ", openid: " + openid + ", unionid: " + unionid + ", nickname: " + nickname
                 + ", headimgurl: " + headimgurl + ", created: " + created;
    }
}