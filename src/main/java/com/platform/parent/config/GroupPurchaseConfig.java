package com.platform.parent.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dengb.
 */
@Component
@ConfigurationProperties
@PropertySource("classpath:config/grouppurchase.properties")
public class GroupPurchaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(GroupPurchaseConfig.class);

    private int deadline, realDeadline;

    private Map<String, BigDecimal> prices;

    private Map<String, String> titles, eventInfos, videoUrls, demoDecriptions;

    private Map<Long, Integer> sessions;

    private Map<String, Integer> quotas;

    private Map<String, Integer> realMemberCounts;

    private Map<String,Integer> description;

    private Map<String,String> descriptions;

    private Map<String,Integer> teacherFlag;

    private Map<String,Integer> schoolFlag;

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public int getRealDeadline() {
        return realDeadline;
    }

    public void setRealDeadline(int realDeadline) {
        this.realDeadline = realDeadline;
    }

    public Map<String, BigDecimal> getPrices() {
        return prices;
    }

    public void setPrices(Map<String, BigDecimal> prices) {
        this.prices = prices;
    }

    public Map<Integer, BigDecimal> getPrice(long courseId)
    {
        Map<String, BigDecimal> prices = this.getPrices();
        Map<Integer, BigDecimal> priceCombibation = new HashMap<Integer, BigDecimal>();
        if (prices != null)
        {
            for (Map.Entry<String, BigDecimal> entry : prices.entrySet())
            {
                String prefix = courseId + "_";
                if (entry.getKey().startsWith(prefix))
                {
                    String priceKey = entry.getKey().substring(prefix.length());
                    if (priceKey != null)
                    {
                        try
                        {
                            int priceKeyInt = Integer.parseInt(priceKey);
                            priceCombibation.put(priceKeyInt, entry.getValue());
                        }
                        catch (Exception exception)
                        {
                            logger.error("Exception when parsing groupon prices: " + exception.getMessage());
                        }
                    }
                }
            }
        }
        return priceCombibation;
    }

    public BigDecimal getPrice(long courseId, int memberCount)
    {
        return this.getPrices().get(getKey(courseId, memberCount));
    }

    public Map<String, String> getEventInfos() {
        return eventInfos;
    }

    public void setEventInfos(Map<String, String> eventInfos) {
        this.eventInfos = eventInfos;
    }

    public String getEventInfo(long courseId, int memberCount)
    {
        return this.getEventInfos().get(getKey(courseId, memberCount));
    }

    public Map<String, String> getTitles() {
        return titles;
    }

    public void setTitles(Map<String, String> titles) {
        this.titles = titles;
    }

    public Map<String, String> getVideoUrls() {
        return videoUrls;
    }

    public void setVideoUrls(Map<String, String> videoUrls) {
        this.videoUrls = videoUrls;
    }

    public String getVideoUrl(long courseId, int memberCount)
    {
        return this.getVideoUrls().get(getKey(courseId, memberCount));
    }

    public Map<String, String> getDemoDecriptions() {
        return demoDecriptions;
    }

    public void setDemoDecriptions(Map<String, String> demoDecriptions) {
        this.demoDecriptions = demoDecriptions;
    }

    public String getDemoDecription(long courseId, int memberCount)
    {
        return this.getDemoDecriptions().get(getKey(courseId, memberCount));
    }

    public String getTitle(long courseId, int memberCount)
    {
        return getTitles().get(getKey(courseId, memberCount));
    }

    public Map<Long, Integer> getSessions() {
        return sessions;
    }

    public void setSessions(Map<Long, Integer> sessions) {
        this.sessions = sessions;
    }

    public Map<String, Integer> getQuotas() {
        return quotas;
    }

    public void setQuotas(Map<String, Integer> quotas) {
        this.quotas = quotas;
    }

    public int getQuota(long courseId, int memberCount)
    {
        Integer quota = this.getQuotas().get(getKey(courseId, memberCount));
        return quota == null ? 0 : quota;
    }

    public Map<String, Integer> getRealMemberCounts() {
        return realMemberCounts;
    }

    public void setRealMemberCounts(Map<String, Integer> realMemberCounts) {
        this.realMemberCounts = realMemberCounts;
    }

    /**
     *
     * @param courseId
     * @param memberCount  it is actually kind of group purchase system ID to identify what group purchase to create
     *                     need to convert it in CourseController to identify the correct group purchase
     *                     and then retrieve realMemberCount with this method
     * @return
     */
    public int getRealMemberCount(long courseId, int memberCount)
    {
        Integer realMemberCount = this.getRealMemberCounts().get(getKey(courseId, memberCount));
        return realMemberCount == null ? 0 : realMemberCount;
    }

    private String getKey(long courseId, int memberCount)
    {
        return courseId  + "_" + memberCount;
    }

    public Map<String, Integer> getDescription() {
        return description;
    }

    public void setDescription(Map<String, Integer> description) {
        this.description = description;
    }

    public Map<String, String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(Map<String, String> descriptions) {
        this.descriptions = descriptions;
    }

    private String getKey(long courseId)
    {
        return String.valueOf(courseId);
    }

    public Integer getDescription(long courseId)
    {
        Integer des = this.getDescription().get(getKey(courseId));
        return des == null ? 0:des;
    }
    public String getDescriptions(long courseId)
    {
        String des = this.getDescriptions().get(getKey(courseId));
        return des == null ? null : des;
    }

    public Map<String, Integer> getTeacherFlag() {
        return teacherFlag;
    }

    public void setTeacherFlag(Map<String, Integer> teacherFlag) {
        this.teacherFlag = teacherFlag;
    }

    public Integer getTeacherFlag(long courseId) {
        Integer des = this.getTeacherFlag().get(getKey(courseId));
        return des == null ? 0 : des;
    }

    public Map<String, Integer> getSchoolFlag() {
        return schoolFlag;
    }

    public void setSchoolFlag(Map<String, Integer> schoolFlag) {
        this.schoolFlag = schoolFlag;
    }

    public Integer getSchoolFlag(long courseId) {
        Integer des = this.getSchoolFlag().get(getKey(courseId));
        return des == null ? 0 : des;
    }
}