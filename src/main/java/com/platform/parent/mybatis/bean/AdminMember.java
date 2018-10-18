package com.platform.parent.mybatis.bean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AdminMember {
    private long id;
    private String openid;
    private String unionid;
    private String nickname;
    private String headimgurl;
    private Timestamp created;

    public static void main(String[] args)
    {
        List<AdminMember> ams = new ArrayList<AdminMember>();

        AdminMember am = new AdminMember();
        am.id(1).headimgurl("head");

        ams.add(am);

        AdminMember am1 = new AdminMember();
        am1.id(2).nickname("nick");
        ams.add(am1);

        System.out.println(ams);
    }

    public AdminMember id(long id) {
        this.id = id;
        return this;
    }

    public AdminMember openid(String openid) {
        this.openid = openid;
        return this;
    }

    public AdminMember unionid(String unionid) {
        this.unionid = unionid;
        return this;
    }

    public AdminMember nickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public AdminMember headimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
        return this;
    }

    public AdminMember created(Timestamp created) {
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

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @Override
    public String toString()
    {
        return "id: " + id + ", openid: " + openid + ", unionid: " + unionid + ", nickname: " + nickname
                 + ", headimgurl: " + headimgurl + ", created: " + created;
    }
}