package com.platform.parent.request.course;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by lsq.
 */
public class GroupPurchaseBookInfo {
    private Integer id;
    private Integer courseid;
    private Integer groupid;
    private String openid;
    private String nickname;
    private String childname;
    private String childage;
    private String childgrade;
    private String phone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCourseid() {
        return courseid;
    }

    public void setCourseid(Integer courseid) {
        this.courseid = courseid;
    }

    public Integer getGroupid() {
        return groupid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
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

    public String getChildname() {
        return childname;
    }

    public void setChildname(String childname) {
        this.childname = childname;
    }

    public String getChildage() {
        return childage;
    }

    public void setChildage(String childage) {
        this.childage = childage;
    }

    public String getChildgrade() {
        return childgrade;
    }

    public void setChildgrade(String childgrade) {
        this.childgrade = childgrade;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
