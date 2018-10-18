package com.platform.parent.mybatis.bean;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by dengb.
 */
public class Course {
    private long id;
    private int sessionCount, passRate, ranking, type, unitType;
    private String title, subtitle, description, adsTitle, adsDescription, teachers, highlight, videoURL, feedbackImgs, introTitle, introSubtitle, unit, unitStr; //audience, goal, outline,
    private double videoSize;
    private BigDecimal originalPrice;
    private List<CourseDetail> details;
    private List<GroupPurchase> groupPurchases;
    private CourseDescription descriptions;
    private Integer teacherFlag;
    private Integer schoolFlag;

    public final static int INTERACTIVE = 1;
    public final static int STAT0C_CONTENT = 2;

    public Course id(long id) {
        this.id = id;
        return this;
    }

    public Course passRate(int passRate) {
        this.passRate = passRate;
        return this;
    }

    public Course sessionCount(int sessionCount) {
        this.sessionCount = sessionCount;
        return this;
    }

    public Course ranking(int ranking) {
        this.ranking = ranking;
        return this;
    }

    public Course type(int type) {
        this.type = type;
        return this;
    }

    public Course unitType(int unitType)
    {
        this.unitType = unitType;
        return this;
    }

    public Course title(String title) {
        this.title = title;
        return this;
    }

    public Course subtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public Course description(String description) {
        this.description = description;
        return this;
    }

    public Course adsTitle(String adsTitle) {
        this.adsTitle = adsTitle;
        return this;
    }

    public Course adsDescription(String adsDescription) {
        this.adsDescription = adsDescription;
        return this;
    }

    public Course teachers(String teachers) {
        this.teachers = teachers;
        return this;
    }

    public Course highlight(String highlight) {
        this.highlight = highlight;
        return this;
    }

    public Course videoURL(String videoURL)
    {
        this.videoURL = videoURL;
        return this;
    }

    public Course videoSize(double videoSize)
    {
        this.videoSize = videoSize;
        return this;
    }

    public Course feedbackImgs(String feedbackImgs)
    {
        this.feedbackImgs = feedbackImgs;
        return this;
    }

    public Course originalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
        return this;
    }

    public Course details(List<CourseDetail> details)
    {
        this.details = details;
        return this;
    }

    public Course groupPurchases(List<GroupPurchase> groupPurchases)
    {
        this.groupPurchases = groupPurchases;
        return this;
    }

    public Course introTitle(String introTitle) {
        this.introTitle = introTitle;
        return this;
    }

    public Course introSubtitle(String introSubtitle) {
        this.introSubtitle = introSubtitle;
        return this;
    }

    public Course unit(String unit) {
        this.unit = unit;
        return this;
    }

    public Course unitStr(String unitStr) {
        this.unitStr = unitStr;
        return this;
    }

    public CourseDescription getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(CourseDescription descriptions) {
        this.descriptions = descriptions;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSessionCount() {
        return sessionCount;
    }

    public void setSessionCount(int sessionCount) {
        this.sessionCount = sessionCount;
    }

    public int getPassRate() {
        return passRate;
    }

    public void setPassRate(int passRate) {
        this.passRate = passRate;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUnitType() {
        return unitType;
    }

    public void setUnitType(int unitType) {
        this.unitType = unitType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdsTitle() {
        return adsTitle;
    }

    public void setAdsTitle(String adsTitle) {
        this.adsTitle = adsTitle;
    }

    public String getAdsDescription() {
        return adsDescription;
    }

    public void setAdsDescription(String adsDescription) {
        this.adsDescription = adsDescription;
    }

    public String getTeachers() {
        return teachers;
    }

    public void setTeachers(String teachers) {
        this.teachers = teachers;
    }

    public String getIntroTitle() {
        return introTitle;
    }

    public void setIntroTitle(String introTitle) {
        this.introTitle = introTitle;
    }

    public String getIntroSubtitle() {
        return introSubtitle;
    }

    public void setIntroSubtitle(String introSubtitle) {
        this.introSubtitle = introSubtitle;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitStr() {
        return unitStr;
    }

    public void setUnitStr(String unitStr) {
        this.unitStr = unitStr;
    }

    //    public String getAudience() {
//        return audience;
//    }
//
//    public void setAudience(String audience) {
//        this.audience = audience;
//    }
//
//    public String getGoal() {
//        return goal;
//    }
//
//    public void setGoal(String goal) {
//        this.goal = goal;
//    }
//
//    public String getOutline() {
//        return outline;
//    }
//
//    public void setOutline(String outline) {
//        this.outline = outline;
//    }

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public double getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(double videoSize) {
        this.videoSize = videoSize;
    }

    public String getFeedbackImgs() {
        return feedbackImgs;
    }

    public void setFeedbackImgs(String feedbackImgs) {
        this.feedbackImgs = feedbackImgs;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public List<CourseDetail> getDetails()
    {
        return details;
    }

    public void setDetails(List<CourseDetail> details)
    {
        this.details = details;
    }

    public List<GroupPurchase> getGroupPurchases() {
        return groupPurchases;
    }

    public void setGroupPurchases(List<GroupPurchase> groupPurchases) {
        this.groupPurchases = groupPurchases;
    }

    public Integer getTeacherFlag() {
        return teacherFlag;
    }

    public void setTeacherFlag(Integer teacherFlag) {
        this.teacherFlag = teacherFlag;
    }

    public Integer getSchoolFlag() {
        return schoolFlag;
    }

    public void setSchoolFlag(Integer schoolFlag) {
        this.schoolFlag = schoolFlag;
    }

    @Override
    public String toString()
    {
        return "id: " + id + ", sessionCount: " + sessionCount  + ", passRate: " + passRate + ", ranking: " + ranking
                + ", type: " + type + ", unityType: " + unitType
                + ", title: " + title  + ", subtitle: " + subtitle + ", description: " + description
                + ", adsTitle: " + adsTitle  + ", adsDescription: " + adsDescription
//                + ", audience: " + audience  + ", goal: " + goal + ", outline: " + outline
                + ", teachers: " + teachers + ", highlight: " + highlight + ", videoURL: " + videoURL
                + ", videoSize: " + videoSize + ", feedbackImgs: " + feedbackImgs
                + ", originalPrice: " + originalPrice + ", details: " + details + ", groupPurchases: " + groupPurchases
                + ", introTitle: " + introTitle + ", introSubtitle: " + introSubtitle
                + ", unit: " + unit + ", unitStr: " + unitStr;
    }
}