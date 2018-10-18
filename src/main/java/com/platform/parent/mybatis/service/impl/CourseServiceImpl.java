package com.platform.parent.mybatis.service.impl;

import com.platform.parent.config.GroupPurchaseConfig;
import com.platform.parent.mybatis.bean.*;
import com.platform.parent.mybatis.dao.CourseMapper;
import com.platform.parent.mybatis.service.CourseService;
import com.platform.parent.request.course.GroupPurchaseBookInfo;
import com.platform.parent.util.WechatUtil;
import com.platform.parent.wxpay.WXPayConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dengb.
 */
@Service
public class CourseServiceImpl implements CourseService {
    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    GroupPurchaseConfig gpConfig;

    @Override
    public List<Course> findCourses() {
        List<Course> courses = this.courseMapper.findCourses();
        if (courses != null)
        {
            for(Course course: courses)
            {
                populateCourse(course, null);
            }
        }
        return courses;
    }

    @Override
    public int countCourseById(long id)
    {
        return this.courseMapper.countCourseById(id);
    }

    @Override
    public Course findCourseById(long id)
    {
        Course course = this.courseMapper.findCourseById(id);
        populateCourse(course, null);
        return course;
    }

    @Override
    public Course findCourseByIdAndUnionid(long id, String unionid)
    {
        Course course = this.courseMapper.findCourseById(id);
        populateCourse(course, unionid);

        return course;
    }

    @Override
    public List<GroupPurchase> findGroupPurchases()
    {
        List<GroupPurchase> groupPurchases = this.courseMapper.findGroupPurchases();
        if (groupPurchases != null)
        {
            for (GroupPurchase gp: groupPurchases)
            {
                populateGroupPurchase(gp);
            }
        }

        return groupPurchases;
    }

    @Override
    public GroupPurchase findGroupPurchaseById(long id)
    {
        GroupPurchase gp = this.courseMapper.findGroupPurchaseById(id);
        populateGroupPurchase(gp);
        return gp;
    }

    @Override
    public List<GroupPurchase> findGroupPurchaseByUnionid(String unionid)
    {
        List<GroupPurchase> groupPurchases = this.courseMapper.findGroupPurchaseByUnionid(unionid);
        if (groupPurchases != null)
        {
            for(GroupPurchase groupPurchase: groupPurchases)
            {
                populateGroupPurchase(groupPurchase);
            }
        }

        return groupPurchases;
    }

    @Override
    public
    List<GroupPurchase> findGroupPurchaseByCourseIdAndUnionid(Map<String, Object> params)
    {
        List<GroupPurchase> groupPurchases = this.courseMapper.findGroupPurchaseByCourseIdAndUnionid(params);

        return groupPurchases;
    }
    private void populateCourse(Course course, String unionid)
    {
        if (course != null)
        {
            long courseId = course.getId();
            Integer sessionCount = gpConfig.getSessions().get(courseId);
            logger.info("CouseId {} has session count {}", courseId, sessionCount);
            if (sessionCount != null)
            {
                course.sessionCount(sessionCount);
            }
            List<CourseDetail> details = this.courseMapper.findCourseDetailById(course.getId());
            course.details(details);
            if (courseId != 0 && courseId >= 15) {
                course.setDescriptions(new CourseDescription(
                        gpConfig.getDescription(courseId), gpConfig.getDescriptions(courseId)));
            }else {
                course.setDescriptions(new CourseDescription(
                        gpConfig.getDescription(0), gpConfig.getDescriptions(0)));
            }
            List<GroupPurchase> groupPurchaseList = new ArrayList<GroupPurchase>();
            course.setTeacherFlag(gpConfig.getTeacherFlag(courseId));
            course.setSchoolFlag(gpConfig.getSchoolFlag(courseId));
            Map<Integer, BigDecimal> prices = gpConfig.getPrice(courseId);
            if (prices != null && !prices.isEmpty())
            {
                course.originalPrice(prices.get(0));

                boolean recommendGroupPurchase = false;
                List<GroupPurchase> groupPurchasesJoined = null;
                if (unionid != null)
                {
                    recommendGroupPurchase = true;
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("courseId", courseId);
                    params.put("unionid", unionid);
                    groupPurchasesJoined = this.findGroupPurchaseByCourseIdAndUnionid(params);
                }

                List<GroupPurchase> availableGPs = this.courseMapper.findAvailableGroupPurchasesByCourseId(courseId);
                for (Map.Entry<Integer, BigDecimal> price: prices.entrySet())
                {
                    if (!price.getKey().equals(0))
                    {
                        // recommend one joined group purchase if possible
                        GroupPurchase recommendedGroupPurchase = null;
                        int memberCount = price.getKey();
                        BigDecimal priceBigDecimal = price.getValue();

                        int quotaAvailable = getGroupPurchaseRemainingQuota(courseId, memberCount);

                        if(groupPurchasesJoined != null && groupPurchasesJoined.size() > 0)
                        {
                            for (GroupPurchase groupPurchaseOwned: groupPurchasesJoined)
                            {
                                if (groupPurchaseOwned.getMembersCount() == memberCount)
                                {
                                    recommendedGroupPurchase = groupPurchaseOwned;
                                    populateGroupPurchase(recommendedGroupPurchase);
                                    logger.info("Recommend {} owned group purchase {} status {}",
                                            unionid, recommendedGroupPurchase.getId(), recommendedGroupPurchase.getStatus());

                                    if (groupPurchaseOwned.getStatus() == GROUP_PURCHASE_OPENED ||
                                            groupPurchaseOwned.getStatus() == GROUP_PURCHASE_COMPLETED)
                                    {
                                        logger.info("Recommend owned group purchase is active, break the loop");
                                        break;
                                    }
                                }
                            }
                        }

                        // recommend one available group purchase if possible,
                        if ((recommendedGroupPurchase == null && recommendGroupPurchase)  // need to recommend group purchase for the unionid user
                                || (recommendedGroupPurchase == null && quotaAvailable == 0))  // 1) quota is used up, no way to create one, need to recommend one
                                                                                               // 2) and no joined group purchase
                                                                                               // only recommend available gp if 1)&2) are true
                                                                                               // in case there is joined group purchase,
                                                                                               // just return the joined group purchase
                        {
                            if (availableGPs != null)
                            {
                                for (GroupPurchase candidate: availableGPs)
                                {
                                    if (candidate.getMembersCount() == memberCount)
                                    {
                                        recommendedGroupPurchase = candidate;
                                        populateGroupPurchase(recommendedGroupPurchase);
                                        logger.info("Recommend available group purchase {}", recommendedGroupPurchase.getId());
                                        break;
                                    }
                                }
                            }
                        }

                        // otherwise make up one empty group purchase
                        if (recommendedGroupPurchase == null)
                        {
                            logger.info("Recommend empty group purchase for member count {}  and price {}", memberCount, priceBigDecimal);
                            recommendedGroupPurchase = new GroupPurchase();
                            recommendedGroupPurchase.courseId(courseId);
                            recommendedGroupPurchase.courseTitle(course.getTitle());
                            recommendedGroupPurchase.membersCount(memberCount);
                            recommendedGroupPurchase.realMembersCount(gpConfig.getRealMemberCount(courseId, memberCount));
                            recommendedGroupPurchase.title(gpConfig.getTitle(courseId, memberCount));
                            recommendedGroupPurchase.price(priceBigDecimal);
                            recommendedGroupPurchase.totalQuota(gpConfig.getQuota(courseId, memberCount));
                            recommendedGroupPurchase.remainingQuota(quotaAvailable);
                            recommendedGroupPurchase.eventInfo(gpConfig.getEventInfo(courseId, memberCount));
                            recommendedGroupPurchase.videoUrl(gpConfig.getVideoUrl(courseId, memberCount));
                            recommendedGroupPurchase.demoDescription(gpConfig.getDemoDecription(courseId, memberCount));
                        }

                        groupPurchaseList.add(recommendedGroupPurchase);
                    }
                }

                course.groupPurchases(groupPurchaseList);
            }
        }
    }

    private void populateGroupPurchase(GroupPurchase gp)
    {
        if(gp != null)
        {
            // group purchase members
            List<GroupPurchaseMember> gpms = this.courseMapper.findGroupPurchaseMembers(gp.getId());
            gp.groupPurchaseMembers(gpms);

            long courseId = gp.getCourseId();
            int memberCount = gp.getMembersCount();

            // title
            gp.title(gpConfig.getTitle(courseId, memberCount));

            // quota
            gp.totalQuota(gpConfig.getQuota(courseId, memberCount));
            gp.remainingQuota(getGroupPurchaseRemainingQuota(courseId, memberCount));

            // envent info
            gp.eventInfo(gpConfig.getEventInfo(courseId, memberCount));

            gp.videoUrl(gpConfig.getVideoUrl(courseId, memberCount));
            gp.demoDescription(gpConfig.getDemoDecription(courseId, memberCount));

            // real members count
            gp.realMembersCount(gpConfig.getRealMemberCount(courseId, memberCount));

            // below logic is implemented in groupurchase_view which return DISMISSED per rea
//            long currentTime = System.currentTimeMillis();
//            logger.info("populateGroupPurchase, group purchase {} status is {}, " +
//                            "deadline is {} and real deadline is {} and current time is {}.",
//                    gp.getId(), gp.getStatus(), gp.getDeadline(), gp.getRealDeadline(), currentTime);
//            if (gp.getStatus() == GROUP_PURCHASE_OPENED)
//            {
//                if(gp.getRealDeadline() != null && gp.getRealDeadline().getTime() <= currentTime)
//                {
//                    logger.info("Update group purchase {} status to dismissed.", gp.getId());
//                    gp.setStatus(CourseService.GROUP_PURCHASE_DISMISSED);
//                }
//            }
        }
    }

    @Override
    public GroupPurchase findGroupPurchaseByCourseUnionidMemberCount(Map<String, Object> params)
    {
        return this.courseMapper.findGroupPurchaseByCourseUnionidMemberCount(params);
    }

    @Override
    public List<GroupPurchaseMember> findGroupPurchaseMembers(long id)
    {
        return this.courseMapper.findGroupPurchaseMembers(id);
    }

    @Override
    public int countMemberByCourseIdAndUnionid(Map<String, Object> params)
    {
        return this.courseMapper.countMemberByCourseIdAndUnionid(params);
    }

    @Override
    public int addGroupPurchase(GroupPurchase groupPurchase)
    {
        return this.courseMapper.addGroupPurchase(groupPurchase);
    }

    @Override
    public int updateGroupPurchase(GroupPurchase groupPurchase)
    {
        int result = this.courseMapper.updateGroupPurchase(groupPurchase);

        if (result == 1 && groupPurchase.getStatus() == GROUP_PURCHASE_COMPLETED)
        {
            List<GroupPurchaseMember> members = findGroupPurchaseMembers(groupPurchase.getId());
            if (members != null && members.size() > 0)
            {
                String courseTitleSuffix = "";
                Course course = this.findCourseById(groupPurchase.getCourseId());
                if (course != null)
                {
                    courseTitleSuffix = "(" + course.getSessionCount() + course.getUnitStr() + ")";
                }

                for (GroupPurchaseMember member: members)
                {
                    logger.info("Send message to {} after purchase group {} completes.", member.getOpenid(), groupPurchase.getId());

                    WXPayConstants.MESSAGE_TEMPLATE_MAP_PURCHASE_GROUP.put("first", WXPayConstants.GREETINGS);
                    WXPayConstants.MESSAGE_TEMPLATE_MAP_PURCHASE_GROUP.put("hotelName", groupPurchase.getCourseTitle() + " " + groupPurchase.getTitle() + " " + courseTitleSuffix);
                    WXPayConstants.MESSAGE_TEMPLATE_MAP_PURCHASE_GROUP.put("voucher number", groupPurchase.getOrderNumber());
                    //getCreated().getTime() + "_" + groupPurchase.getId());
                    WXPayConstants.MESSAGE_TEMPLATE_MAP_PURCHASE_GROUP.put("remark", groupPurchase.isInterative() ? "\n客户专员会尽快与您联系，请稍候。"
                            : (groupPurchase.isStaticContent() ? "\n点击开始上课。" : ""));
                    WechatUtil.sendMessage(groupPurchase, member, WXPayConstants.MESSAGE_TEMPLATE_PURCHASE_GROUP,
                            WXPayConstants.MESSAGE_TEMPLATE_MAP_PURCHASE_GROUP, member.getChannel());
                }
            }
        }

        return result;
    }

    @Override
    public int addGroupPurchaseMember(GroupPurchaseMember groupPurchaseMember, String channel)
    {
        int result =  this.courseMapper.addGroupPurchaseMember(groupPurchaseMember);
        logger.info("addGroupPurchaseMember, result {}.", result);
        if (result == 1)
        {
            GroupPurchase groupPurchase = this.findGroupPurchaseById(groupPurchaseMember.getGroupId());
            logger.info("got groupPurchase {}", groupPurchase);
            if (groupPurchase != null)
            {
                int currentMembers = this.courseMapper.findGroupPurchaseMembers(groupPurchase.getId()).size();
                int membersCount = groupPurchase.getRealMembersCount();
                int remaingCount = membersCount - currentMembers;
                logger.info("Send message to {} after purchasing.", groupPurchaseMember.getOpenid());
                String courseTitleSuffix = "";
                Course course = this.findCourseById(groupPurchase.getCourseId());
                if (course != null)
                {
                    courseTitleSuffix = "(" + course.getSessionCount() + course.getUnitStr() + ")";
                }
                WXPayConstants.MESSAGE_TEMPLATE_MAP_PURCHASE.put("name", groupPurchase.getCourseTitle() + " " + groupPurchase.getTitle() + " " + courseTitleSuffix);
                WXPayConstants.MESSAGE_TEMPLATE_MAP_PURCHASE.put("remark", remaingCount == 0 ? "" :
                        "\n" + membersCount + "人成团，还差" + (membersCount - currentMembers) + "人，快快邀请好友来拼团吧！");
                WechatUtil.sendMessage(groupPurchase, groupPurchaseMember, WXPayConstants.MESSAGE_TEMPLATE_PURCHASE,
                        WXPayConstants.MESSAGE_TEMPLATE_MAP_PURCHASE, channel);
            }
        }

        return result;
    }

    @Override
    public int getGroupPurchaseRemainingQuota(long courseId, int membersCount)
    {
        int quota = gpConfig.getQuota(courseId, membersCount);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("courseId", courseId);
        params.put("membersCount", membersCount);
        logger.info("Count active group purchase for params: " + params);
        int quotaConsumed = this.courseMapper.countActiveGroupPurchases(params);

        return quota - quotaConsumed;
    }

    @Override
    public boolean isPayed(String openid)
    {
        if (openid == null || openid.isEmpty())
        {
            logger.info("In isPayed: invalid openid {}", openid);
            return false;
        }

        int gpPayments = this.courseMapper.countGroupPurchasePaymentByOpenid(openid);
        int gpRefunds = this.courseMapper.countGroupPurchaseRefundByOpenid(openid);
        int gpMembers = this.courseMapper.countGroupPurchasePurchaseMemberByOpenid(openid);

        logger.info("In isPayed: openid {} : gppayments is {}, gpRefund is {}, gpMembers is {}.", openid, gpPayments, gpRefunds, gpMembers);

        return ( gpMembers + 1 ) <= ( gpPayments - gpRefunds );
    }

    @Override
    public AdminMember findAdminMemberById(long id)
    {
        return this.courseMapper.findAdminMemberById(id);
    }

    @Override
    public List<AdminMember> findAdminMembers()
    {
        return this.courseMapper.findAdminMembers();
    }

    @Override
    public int addAdminMember(AdminMember adminMember)
    {
        return this.courseMapper.addAdminMember(adminMember);
    }

    @Override
    public int deleteAdminMember(AdminMember adminMember)
    {
        return this.courseMapper.deleteAdminMember(adminMember);
    }

    @Override
    public AdminMember findAdminCandidateById(long id)
    {
        return this.courseMapper.findAdminCandidateById(id);
    }

    @Override
    public List<AdminMember> findAdminCandidates()
    {
        return this.courseMapper.findAdminCandidates();
    }

    @Override
    public int addAdminCandidate(AdminMember adminMember)
    {
        return this.courseMapper.addAdminCandidate(adminMember);
    }

    @Override
    public int deleteAdminCandidate(AdminMember adminMember)
    {
        return this.courseMapper.deleteAdminCandidate(adminMember);
    }

    @Override
    public int addGroupPurchaseBookInfo(GroupPurchaseBookInfo info)
    {
        int result =  this.courseMapper.addGroupPurchaseBookInfo(info);
        logger.info("addGroupPurchaseBookInfo, result {}.", result);
        return result;
    }

}