package com.platform.parent.mybatis.dao;

import com.platform.parent.mybatis.bean.*;
import com.platform.parent.request.course.GroupPurchaseBookInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by dengb.
 */
@Mapper
@Component
public interface CourseMapper {
    List<Course> findCourses();
    int countCourseById(long id);
    Course findCourseById(long id);
    List<GroupPurchase> findGroupPurchases();
    GroupPurchase findGroupPurchaseById(long id);
    List<GroupPurchase> findGroupPurchaseByUnionid(String unionid);
    int countActiveGroupPurchases(Map<String, Object> params);
    List<GroupPurchase> findGroupPurchaseByCourseIdAndUnionid(Map<String, Object> params);
    List<GroupPurchase> findAvailableGroupPurchasesByCourseId(long courseId);
    List<GroupPurchaseMember> findGroupPurchaseMembers(long id);
    GroupPurchase findGroupPurchaseByCourseUnionidMemberCount(Map<String, Object> params);
    int countMemberByCourseIdAndUnionid(Map<String, Object> params);
    int addGroupPurchase(GroupPurchase groupPurchase);
    int updateGroupPurchase(GroupPurchase groupPurchase);
    int addGroupPurchaseMember(GroupPurchaseMember groupPurchaseMember);

    List<CourseDetail> findCourseDetailById(long courseId);

    int addGroupPurchasePayment(GroupPurchasePayment groupPurchasePayment);

    int deleteGroupPurchaseMember();
    int deleteGroupPurchase();

    int countGroupPurchaseMember();
    int countGroupPurchasePayment();

    int countGroupPurchasePaymentByOpenid(String openid);
    int countGroupPurchaseRefundByOpenid(String openid);
    int countGroupPurchasePurchaseMemberByOpenid(String openid);

    int addGroupPurchaseRefund(GroupPurchaseRefund groupPurchaseRefund);
    List<GroupPurchaseRefund> findGroupPurchaseRefundCandidates();
    List<GroupPurchaseRefund> findGroupPurchaseRefund();

    int findAdminMember(String unionid);

    AdminMember findAdminMemberById(long id);
    List<AdminMember> findAdminMembers();
    int addAdminMember(AdminMember adminMember);
    int deleteAdminMember(AdminMember adminMember);

    AdminMember findAdminCandidateById(long id);
    List<AdminMember> findAdminCandidates();
    int addAdminCandidate(AdminMember adminCandidate);
    int deleteAdminCandidate(AdminMember adminCandidate);

    int addGroupPurchaseBookInfo(GroupPurchaseBookInfo groupPurchaseBookInfo);

    int addOpenIdAndParam(@Param(value = "param") String param,
                          @Param(value = "openid") String openid);

    String getOpenId(@Param(value = "param")String param);

    int updateGroupMember(@Param(value = "nickname")String nickname,
                          @Param(value = "groupid") Integer groupid,
                          @Param(value = "openid") String openid);

    /**
     * H5支付,用户授权后更新真正名称和头像
     *
     * @param nickname
     * @param headimgurl
     * @param openid
     * @return
     */
    int updateMemberByUserInfo(@Param(value = "unionid") String unionid,
                               @Param(value = "openid") String openid,
                               @Param(value = "nickname")String nickname,
                          @Param(value = "headimgurl") String headimgurl);

    /**
     * 查询H5支付记录
     *
     * @param openid
     * @return
     */
    int getH5PaymentCount(@Param(value = "openid")String openid);

    List<GroupPurchase> findGroupPurchaseByUnionidAndCourseId(@Param(value = "unionId") String unionid,
                                                                  @Param(value = "courseId")String courseId);

}
