package com.platform.parent.request.course;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by dengb.
 */
public class JoinGroupPurchase {
    @Min(1)
    private long groupPurchaseId;

    @NotNull
    private String unionid;

    private String openid;
    private String nickname;
    private String headimgurl;

    public long getGroupPurchaseId() {
        return groupPurchaseId;
    }

    public void setGroupPurchaseId(long groupPurchaseId) {
        this.groupPurchaseId = groupPurchaseId;
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
        return "groupPurchaseId: " + groupPurchaseId + ", unionid:" + unionid + ", openid: " + openid
                + ", nickname: " + nickname + ", headimgurl: " + headimgurl;
    }
}
