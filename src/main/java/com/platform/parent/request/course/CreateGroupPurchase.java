package com.platform.parent.request.course;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by dengb.
 */
public class CreateGroupPurchase {
    @Min(1)
    private long courseId;
    @Min(1)
    private int membersCount;
    @NotNull
    private String unionid;
    @NotNull
    private String openid;
    private String nickname;
    private String headimgurl;

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public int getMembersCount() {
        return membersCount;
    }

    public void setMembersCount(int membersCount) {
        this.membersCount = membersCount;
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

    @Override
    public String toString()
    {
        return "courseId: " + courseId + ", membersCount: " + membersCount + ", unionid: " + unionid
                + ", openid: " + openid + ", nickname: " + nickname + ", headimgurl: " + headimgurl;
    }
}
