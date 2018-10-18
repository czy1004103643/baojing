package com.platform.parent.controller;

import com.alibaba.fastjson.JSONObject;
import com.platform.parent.config.GroupPurchaseConfig;
import com.platform.parent.mybatis.bean.Course;
import com.platform.parent.mybatis.bean.GroupPurchase;
import com.platform.parent.mybatis.bean.GroupPurchaseMember;
import com.platform.parent.mybatis.dao.CourseMapper;
import com.platform.parent.mybatis.service.CourseService;
import com.platform.parent.request.course.CreateGroupPurchase;
import com.platform.parent.request.course.GroupPurchaseBookInfo;
import com.platform.parent.request.course.JoinGroupPurchase;
import com.platform.parent.util.EnumUtil;
import com.platform.parent.util.ErrorCode;
import com.platform.parent.util.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dengb.
 */
@RestController
@RequestMapping(value = "/course")
public class CourseController {
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    CourseService courseService;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    GroupPurchaseConfig gpConfig;

    /**
     * 返回课程拼团信息
     *详情见文档
     *
     * @param id
     * @param unionid
     * @return
     */
    @RequestMapping(value = "/instance", method = RequestMethod.GET)
    public @ResponseBody Object getCourse(@RequestParam(required = false, value = "id") Long id,
                                          @RequestParam(required = false, value = "unionid") String unionid) {
        logger.info("Course instance, id: " + id + ", unionid: " + unionid);
        List<Course> courses = null;
        Course course = null;
        if (id == null)
        {
            courses = this.courseService.findCourses();
        }
        else
        {
            if (unionid != null)
            {
                course = this.courseService.findCourseByIdAndUnionid(id, unionid);
            }
            else
            {
                course = this.courseService.findCourseById(id);
            }
        }

        JSONObject result = new JSONObject();
        result.put("status","0");
        result.put("message","成功");
        JSONObject data = new JSONObject();
        if (courses != null)
        {
            data.put("courses", courses);
        }
        else if (course != null)
        {
            data.put("course", course);
        }
        result.put("data", data);
        
        if (course != null)
        {
            logger.info("Got course: " + course.getId());
        }
        
        if (courses != null)
        {
            logger.info("Got courses: " + courses.size());
        }
        return result;
    }

    @RequestMapping(value = "/groupon/instance", method = RequestMethod.GET)
    public @ResponseBody Object getGroupPurchase(@RequestParam(required = false, value = "id") Long id,
                                                 @RequestParam(required = false, value = "unionid") String unionid) {
        logger.info("getGroupPurchase for id: [" + id + "]" + " and unionid: " + unionid);
        GroupPurchase groupPurchase = null;
        List<GroupPurchase> groupPurchases = null;
        if (id != null)
        {
            groupPurchase = this.courseService.findGroupPurchaseById(id);
        }
        else if (unionid != null)
        {
            groupPurchases = this.courseService.findGroupPurchaseByUnionid(unionid);
        }
        else
        {
            groupPurchases = this.courseService.findGroupPurchases();
        }

        JSONObject result = new JSONObject();
        result.put("status","0");
        result.put("message","成功");
        JSONObject data = new JSONObject();
        if (groupPurchase != null)
        {
            data.put("groupPurchase", groupPurchase);
        }
        else if (groupPurchases != null)
        {
            data.put("groupPurchases", groupPurchases);
        }
        result.put("data", data);

        if (groupPurchase != null)
        {
            logger.info("Got groupPurchase: " + groupPurchase.getId());
        }
        
        if (groupPurchases != null)
        {
            logger.info("Got groupPurchases: " + groupPurchases.size());
        }
        return result;
    }

    /**
     * 开团
     * @param request
     * @param req
     * @return
     */
    @RequestMapping(value = "/groupon/create", method = RequestMethod.POST)
    @ResponseBody @Transactional
    public Object createGroupPurchase(HttpServletRequest request, @Valid @RequestBody CreateGroupPurchase req) {
        logger.info("Create group purchase request: " + req);
        long courseId = req.getCourseId();
        int memberCount = req.getMembersCount();
        BigDecimal price = gpConfig.getPrice(courseId, memberCount);
        if (price == null)
        {
            logger.error("No group purchase price available for courseId " + courseId + " and member count " + memberCount);
            return EnumUtil.errorToJson(ErrorCode.GROUP_PURCHASE_MEMBER_CONFIG_NOT_AVAILABLE);
        }

        String titleConfigured = gpConfig.getTitle(courseId, memberCount);
        BigDecimal priceConfigured = gpConfig.getPrice(courseId, memberCount);
        String eventInfoConfigured = gpConfig.getEventInfo(courseId, memberCount);
        if (titleConfigured == null || priceConfigured == null || eventInfoConfigured == null)
        {
            logger.error("No group purchase configuration available for courseId " + courseId + " and member count " + memberCount
            + ". title: " + titleConfigured + ", price: " + priceConfigured + ", eventInfo: " + eventInfoConfigured);
            return EnumUtil.errorToJson(ErrorCode.GROUP_PURCHASE_MEMBER_CONFIG_NOT_AVAILABLE);
        }

        int count = this.courseService.countCourseById(courseId);
        if (count == 0) {
            logger.error("Find course for id:{} error, no such course", courseId);
            return EnumUtil.errorToJson(ErrorCode.NO_SUCH_COURSE);
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("courseId", courseId);
        params.put("unionid",req.getUnionid());
        params.put("membersCount",memberCount);
        count = this.courseService.countMemberByCourseIdAndUnionid(params);
        //以参团或者以创建团
        if (count != 0) {
            logger.error("User with unionid :{} already opened or joined group purchase for course {}",
                    req.getUnionid(), courseId);
            return EnumUtil.errorToJson(ErrorCode.ALREADY_CREATED_OR_JOINED_GROUP_PURCHASE_PER_COURSE_MEMBER);
        }

        int quotaAvail = this.courseService.getGroupPurchaseRemainingQuota(courseId, memberCount);
        if (quotaAvail == 0)
        {
            logger.error("Course with id:{} for member count {} quota reached.",
                    courseId, memberCount);
            return EnumUtil.errorToJson(ErrorCode.COURSE_GROUP_PURCHASE_QUOTA_REACHED);
        }

        String openid = req.getOpenid();
        if (!this.courseService.isPayed(openid))
        {
            logger.error("unionid {} and openid {} not payed yet for course {} and memberCount {}.",
                    req.getUnionid(), openid, courseId, memberCount);
            return EnumUtil.errorToJson(ErrorCode.GROUP_PURCHASE_NOT_PAYED);
        }

        long currentTime = System.currentTimeMillis();
        long deadlineTime = currentTime + 1000 * 3600 * 24 * gpConfig.getDeadline();
        long realDeadlineTime = currentTime + 1000 * 3600 * 24 * gpConfig.getRealDeadline();
        Timestamp created = new Timestamp(currentTime);
        Timestamp deadline = new Timestamp(deadlineTime);
        Timestamp realDeadline = new Timestamp(realDeadlineTime);

        GroupPurchase gp = new GroupPurchase();

        // memberCount was memberCount....  later it became sort of group purchase system ID
        // so we need to find the REAL memberCount of the gourp purchase here
        int realMemberCount = gpConfig.getRealMemberCount(courseId, memberCount);
        gp.courseId(courseId).ownerUnionid(req.getUnionid()).membersCount(memberCount).realMembersCount(realMemberCount).
                price(price).created(created).deadline(deadline).realDeadline(realDeadline).
                status(CourseService.GROUP_PURCHASE_OPENED);

        int tmpResult = this.courseService.addGroupPurchase(gp);
        if (tmpResult <= 0) {
            logger.error("Create group purchase failed. Detail: " + gp);
            return EnumUtil.errorToJson(ErrorCode.CREATE_GROUP_PURCHASE_FAILED);
        }

        GroupPurchase createdGp = this.courseService.findGroupPurchaseByCourseUnionidMemberCount(params);
        if (createdGp == null)
        {
            logger.error("Failed to find the newly created group purchase for user:{}",
                    req.getUnionid());
            return EnumUtil.errorToJson(ErrorCode.NEWLY_CREATED_GROUP_PURCHASE_NOT_FOUND);
        }
        long gpId = createdGp.getId();
        logger.info("Newly created group purchase {}.", gpId);

        GroupPurchaseMember gpm = new GroupPurchaseMember();
        String channel = HttpClientUtil.getChannel(request);
        String outTradeNo = HttpClientUtil.getOutTradeNo(request);
        gpm.unionid(req.getUnionid()).openid(openid).nickname(req.getNickname())
                .headimgurl(req.getHeadimgurl()).joined(created).groupId(gpId).channel(channel).outTradeNo(outTradeNo);
        tmpResult = this.courseService.addGroupPurchaseMember(gpm, channel);
        if (tmpResult <= 0) {
            logger.error("Create group purchase member failed. Detail: " + gpm);
            return EnumUtil.errorToJson(ErrorCode.CREATE_GROUP_PURCHASE_MEMBER_FAILED);
        }

        JSONObject result = new JSONObject();
        JSONObject data = new JSONObject();
        result.put("status", 200);
        result.put("message", "成功");
        data.put("id", gpId);
        result.put("data",data);
        return result;
    }

    /**
     * 参团
     *
     * @param request
     * @param req
     * @return
     */
    @RequestMapping(value = "/groupon/join", method = RequestMethod.POST)
    @ResponseBody @Transactional
    public Object joinGroupPurchase(HttpServletRequest request, @Valid @RequestBody JoinGroupPurchase req) {
        logger.info("Join group purchase request: " + req);
        //找到拼团信息
        GroupPurchase gp = this.courseService.findGroupPurchaseById(req.getGroupPurchaseId());
        if (gp == null) {
            logger.error("Find course for id:{} error, no such group purchase", req.getGroupPurchaseId());
            return EnumUtil.errorToJson(ErrorCode.NO_SUCH_GROUP_PURCHASE);
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("courseId", gp.getCourseId());
        params.put("unionid",req.getUnionid());
        params.put("membersCount",gp.getMembersCount());
        //看有没有拼过团
        int count = this.courseService.countMemberByCourseIdAndUnionid(params);
        if (count != 0) {
            logger.error("User with unionid :{} already opened or joined group purchase for course id {}",
                    req.getUnionid(), gp.getCourseId());
            return EnumUtil.errorToJson(ErrorCode.ALREADY_CREATED_OR_JOINED_GROUP_PURCHASE_PER_COURSE_MEMBER);
        }

        if (gp.getStatus() == CourseService.GROUP_PURCHASE_COMPLETED)
        {
            logger.error("Group purchase with id :{} already completed", req.getGroupPurchaseId());
            return EnumUtil.errorToJson(ErrorCode.GROUP_PURCHASE_COMPLETED);
        }

        if (gp.getStatus() == CourseService.GROUP_PURCHASE_DISMISSED)
        {
            logger.error("Group purchase with id :{} already dismissed", req.getGroupPurchaseId());
            return EnumUtil.errorToJson(ErrorCode.GROUP_PURCHASE_DISMISSED);
        }

        String outTradeNo = HttpClientUtil.getOutTradeNo(request);
        if (gp.getRealDeadline().getTime() <= System.currentTimeMillis())
        {
            logger.error("Group purchase with id :{} already expired, update its status to dismissed.", req.getGroupPurchaseId());
            gp.status(CourseService.GROUP_PURCHASE_DISMISSED);
            this.courseService.updateGroupPurchase(gp);
            return EnumUtil.errorToJson(ErrorCode.GROUP_PURCHASE_DISMISSED);
        }
        //未支付
        if (!this.courseService.isPayed(req.getOpenid()))
        {
            logger.error("unionid {} and openid {} not payed yet for course {} and memberCount {}.",
                    req.getUnionid(), req.getOpenid(), gp.getCourseId(), gp.getMembersCount());
            return EnumUtil.errorToJson(ErrorCode.GROUP_PURCHASE_NOT_PAYED);
        }

        logger.info("Before joining group purchase {} with member count {} with real member count {}, it has {} existing members.",
                gp.getId(), gp.getMembersCount(), gp.getRealMembersCount(), gp.getGroupPurchaseMembers() == null ? 0 : gp.getGroupPurchaseMembers().size());

        String channel = HttpClientUtil.getChannel(request);
        GroupPurchaseMember gpm = new GroupPurchaseMember();
        gpm.unionid(req.getUnionid()).openid(req.getOpenid()).nickname(req.getNickname()).headimgurl(req.getHeadimgurl())
                .joined(new Timestamp(System.currentTimeMillis())).groupId(req.getGroupPurchaseId()).channel(channel).outTradeNo(outTradeNo);
        int tmpResult = this.courseService.addGroupPurchaseMember(gpm, channel);
        //加团失败
        if (tmpResult <= 0) {
            logger.error("Create group purchase member failed. Detail: " + gpm);
            return EnumUtil.errorToJson(ErrorCode.CREATE_GROUP_PURCHASE_MEMBER_FAILED);
        }

        List<GroupPurchaseMember> members = this.courseService.findGroupPurchaseMembers(req.getGroupPurchaseId());
        int membersCount = members == null ? 0 : members.size();
        logger.info("After joining group purchase {} with member count {} with real member count {}, it has {} existing members.",
                gp.getId(), gp.getMembersCount(), gp.getRealMembersCount(), membersCount);
        if (membersCount == gp.getRealMembersCount())
        {
            gp.status(CourseService.GROUP_PURCHASE_COMPLETED);
            gp.completed(new Timestamp(System.currentTimeMillis()));
            tmpResult = this.courseService.updateGroupPurchase(gp);
            if (tmpResult <= 0) {
                //更新状态失败
                logger.error("Update group purchase member failed. Detail: " + gp);
                return EnumUtil.errorToJson(ErrorCode.UPDATE_GROUP_PURCHASE_FAILED);
            }
        }

        JSONObject result = new JSONObject();
        JSONObject data = new JSONObject();
        result.put("status", 200);
        result.put("message", "成功");
        data.put("unionid", req.getUnionid());
        data.put("groupPurchaseId", gp.getId());
        result.put("data",data);
        return result;
    }

    @RequestMapping(value = "/be_careful", method = RequestMethod.GET)
    public @ResponseBody Object be_careful() {
        logger.info("be_careful, deleting grpoup purchase and grpoup purchase members...");

        int gpmDeleted = this.courseMapper.deleteGroupPurchaseMember();
        int gpDeleted = this.courseMapper.deleteGroupPurchase();

        JSONObject result = new JSONObject();
        JSONObject data = new JSONObject();
        result.put("status", 200);
        result.put("message", "成功");
        data.put("gpmDeleted", gpmDeleted);
        data.put("gpDeleted", gpDeleted);
        result.put("data",data);
        return result;
    }

    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public @ResponseBody Object check() {
        logger.info("check gp members and payment...");

        int members = this.courseMapper.countGroupPurchaseMember();
        int payments = this.courseMapper.countGroupPurchasePayment();
        int refundCandidates = this.courseMapper.findGroupPurchaseRefundCandidates().size();
        int refunds = this.courseMapper.findGroupPurchaseRefund().size();

        JSONObject result = new JSONObject();
        JSONObject data = new JSONObject();
        result.put("status", 200);
        result.put("message", "成功");
        data.put("members", members);
        data.put("payments", payments);
        data.put("refundCandidates", refundCandidates);
        data.put("refunds", refunds);
        result.put("data",data);
        return result;
    }

    /**
     * 预约信息
     *
     * @param info
     * @return
     */
    @RequestMapping(value = "/group/book", method = RequestMethod.POST)
    @ResponseBody @Transactional
    public Object groupPurchaseBook( @Valid @RequestBody GroupPurchaseBookInfo info) {
        logger.info("group purchase book: " + info);
//        if (info.getCourseid() == null || info.getCourseid() < 1){
//            return EnumUtil.errorToJson(ErrorCode.NO_SUCH_COURSE);
//        }
        //找到拼团信息
        if (info.getGroupid() != null) {
            GroupPurchase gp = this.courseMapper.findGroupPurchaseById(info.getGroupid());
            if (gp == null) {
                logger.error("Find group purchase for id:{} error, no such course", info.getGroupid());
                return EnumUtil.errorToJson(ErrorCode.NO_SUCH_GROUP_PURCHASE);
            }
        }
        if (info.getCourseid() != null) {
            Course course = this.courseMapper.findCourseById(info.getCourseid());
            if (course == null) {
                logger.error("Find course for id:{} error, no such course", info.getCourseid());
                return EnumUtil.errorToJson(ErrorCode.NO_SUCH_COURSE);
            }
        }
        Integer resultNum = this.courseService.addGroupPurchaseBookInfo(info);
        if (resultNum <= 0) {
            logger.error("Create group purchase member failed. Detail: " + resultNum);
            return EnumUtil.errorToJson(ErrorCode.CREATE_GROUP_PURCHASE_BOOK_INFO_FAILED);
        }
        //更新参团信息
        if (info.getGroupid() != null) {
            Integer num = this.courseMapper.updateGroupMember(info.getChildname(), info.getGroupid(), info.getOpenid());
        }
        JSONObject result = new JSONObject();
        JSONObject data = new JSONObject();
        result.put("status", 200);
        result.put("message", "成功");
        data.put("openid", info.getOpenid());
        data.put("infoid", info.getGroupid());
        result.put("data",data);
        return result;
    }

    @RequestMapping(value = "/getOpenid", method = RequestMethod.GET)
    public @ResponseBody Object getOpenid(String param) {
        logger.info("getOpenid...param="+param);

        String openid = this.courseMapper.getOpenId(param);

        JSONObject result = new JSONObject();
        result.put("status", 200);
        result.put("message", "成功");
        result.put("openid",openid);
        return result;
    }
}
