package com.platform.parent.mybatis.service;

import com.platform.parent.mybatis.bean.*;
import com.platform.parent.request.course.GroupPurchaseBookInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by dengb.
 */
public interface CourseService {
    public final static int GROUP_PURCHASE_NOT_OPENED = 0;
    public final static int GROUP_PURCHASE_OPENED = 1;
    public final static int GROUP_PURCHASE_COMPLETED = 2;
    public final static int GROUP_PURCHASE_DISMISSED = 3;

    public final static int UNIT_TYPE_MONTH = 1;

    List<Course> findCourses();
    int countCourseById(long id);
    Course findCourseById(long id);
    Course findCourseByIdAndUnionid(long id, String unionid);
    List<GroupPurchase> findGroupPurchases();
    GroupPurchase findGroupPurchaseById(long id);
    List<GroupPurchase> findGroupPurchaseByUnionid(String unionid);
    List<GroupPurchase> findGroupPurchaseByCourseIdAndUnionid(Map<String, Object> params);
    // return list as created group purchase may have expired
    GroupPurchase findGroupPurchaseByCourseUnionidMemberCount(Map<String, Object> params);
    List<GroupPurchaseMember> findGroupPurchaseMembers(long id);
    int countMemberByCourseIdAndUnionid(Map<String, Object> params);
    int addGroupPurchase(GroupPurchase groupPurchase);
    int updateGroupPurchase(GroupPurchase groupPurchase);
    int addGroupPurchaseMember(GroupPurchaseMember groupPurchaseMember, String channel);
    int getGroupPurchaseRemainingQuota(long courseId, int membersCount);

    boolean isPayed(String openid);

    AdminMember findAdminMemberById(long id);
    int addAdminMember(AdminMember adminMember);
    int deleteAdminMember(AdminMember adminMember);
    List<AdminMember> findAdminMembers();

    AdminMember findAdminCandidateById(long id);
    int addAdminCandidate(AdminMember adminCandidate);
    int deleteAdminCandidate(AdminMember adminCandidate);
    List<AdminMember> findAdminCandidates();

    int addGroupPurchaseBookInfo(GroupPurchaseBookInfo info);
}
