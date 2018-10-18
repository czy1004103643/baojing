package com.platform.parent.mybatis.bean;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by dengb.
 */
public class GroupPurchase {
    private long id, courseId;
    private int membersCount, status, totalQuota, remainingQuota, type;
    // realMembersCount represent how many members this group purchase contains
    // Note membersCount was intended to represent how many members this group purchase contains.
    // however, later on, it turned out different group purchase may contain same members count.
    // therefore now membersCount was sort of system ID of group purchase while realMembersCount is the members count
    private int realMembersCount;
    private BigDecimal price;
    private Timestamp  created, deadline, realDeadline, completed;
    private String title, courseTitle, ownerUnionid, eventInfo, orderNumber, videoUrl, demoDescription;

    private List<GroupPurchaseMember> groupPurchaseMembers;

    public GroupPurchase id(long id) {
        this.id = id;
        return this;
    }

    public GroupPurchase courseId(long courseId) {
        this.courseId = courseId;
        return this;
    }

    public GroupPurchase membersCount(int membersCount) {
        this.membersCount = membersCount;
        return this;
    }

    public GroupPurchase realMembersCount(int realMembersCount) {
        this.realMembersCount = realMembersCount;
        return this;
    }

    public GroupPurchase status(int status) {
        this.status = status;
        return this;
    }

    public GroupPurchase totalQuota(int totalQuota) {
        this.totalQuota = totalQuota;
        return this;
    }

    public GroupPurchase remainingQuota(int remainingQuota) {
        this.remainingQuota = remainingQuota;
        return this;
    }

    public GroupPurchase type(int type) {
        this.type = type;
        return this;
    }

    public GroupPurchase price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public GroupPurchase created(Timestamp created) {
        this.created = created;
        return this;
    }

    public GroupPurchase deadline(Timestamp deadline) {
        this.deadline = deadline;
        return this;
    }

    public GroupPurchase realDeadline(Timestamp realDeadline) {
        this.realDeadline = realDeadline;
        return this;
    }

    public GroupPurchase completed(Timestamp completed) {
        this.completed = completed;
        return this;
    }

    public GroupPurchase title(String title)
    {
        this.title = title;
        return this;
    }

    public GroupPurchase courseTitle(String courseTitle)
    {
        this.courseTitle = courseTitle;
        return this;
    }

    public GroupPurchase ownerUnionid(String ownerUnionid) {
        this.ownerUnionid = ownerUnionid;
        return this;
    }

    public GroupPurchase eventInfo(String eventInfo) {
        this.eventInfo = eventInfo;
        return this;
    }

    public GroupPurchase orderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public GroupPurchase groupPurchaseMembers(List<GroupPurchaseMember> groupPurchaseMembers) {
        this.groupPurchaseMembers = groupPurchaseMembers;
        return this;
    }

    public GroupPurchase videoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }

    public GroupPurchase demoDescription(String demoDescription) {
        this.demoDescription = demoDescription;
        return this;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public int getRealMembersCount() {
        return realMembersCount;
    }

    public void setRealMembersCount(int realMembersCount) {
        this.realMembersCount = realMembersCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotalQuota() {
        return totalQuota;
    }

    public void setTotalQuota(int totalQuota) {
        this.totalQuota = totalQuota;
    }

    public int getRemainingQuota() {
        return remainingQuota;
    }

    public void setRemainingQuota(int remainingQuota) {
        this.remainingQuota = remainingQuota;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    public Timestamp getRealDeadline() {
        return realDeadline;
    }

    public void setRealDeadline(Timestamp realDeadline) {
        this.realDeadline = realDeadline;
    }

    public Timestamp getCompleted() {
        return completed;
    }

    public void setCompleted(Timestamp completed) {
        this.completed = completed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getOwnerUnionid() {
        return ownerUnionid;
    }

    public void setOwnerUnionid(String ownerUnionid) {
        this.ownerUnionid = ownerUnionid;
    }

    public String getEventInfo() {
        return eventInfo;
    }

    public void setEventInfo(String eventInfo) {
        this.eventInfo = eventInfo;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public List<GroupPurchaseMember> getGroupPurchaseMembers() {
        return groupPurchaseMembers;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDemoDescription() {
        return demoDescription;
    }

    public void setDemoDescription(String demoDescription) {
        this.demoDescription = demoDescription;
    }

    public void setGroupPurchaseMembers(List<GroupPurchaseMember> groupPurchaseMembers) {
        this.groupPurchaseMembers = groupPurchaseMembers;
    }

    public boolean isInterative()
    {
        return type == Course.INTERACTIVE;
    }

    public boolean isStaticContent()
    {
        return type == Course.STAT0C_CONTENT;
    }

    @Override
    public String toString()
    {
        return "id: " + id + ", courseId: " + courseId  + ", membersCount: " + membersCount + ", realMembersCount: " + realMembersCount + ", status: " + status
                + ", totalQuota: " + totalQuota + ", remainingQuota: " + remainingQuota + ", price: " + price
                + ", created: " + created + ", deadline: " + deadline + ", realDeadline: " + realDeadline
                + ", completed: " + completed + ", title: " + title + ", courseTitle: " + courseTitle
                + ", ownerUnionid: " + ownerUnionid + ", eventInfo: " + eventInfo
                + ", orderNumber: " + orderNumber + ", type: " + type
                + ", videoUrl: " + videoUrl + ", demoDescription: " + demoDescription + "\n";
    }
}