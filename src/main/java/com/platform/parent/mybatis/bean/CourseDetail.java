package com.platform.parent.mybatis.bean;

/**
 * Created by dengb.
 */
public class CourseDetail {
//    private long id, courseId;
    private String level, audience, goal, outline;

//    public CourseDetail id(long id) {
//        this.id = id;
//        return this;
//    }
//
//    public CourseDetail courseId(long courseId) {
//        this.courseId = courseId;
//        return this;
//    }

    public CourseDetail level(String level) {
        this.level = level;
        return this;
    }

    public CourseDetail audience(String audience) {
        this.audience = audience;
        return this;
    }

    public CourseDetail goal(String goal) {
        this.goal = goal;
        return this;
    }

    public CourseDetail outline(String outline) {
        this.outline = outline;
        return this;
    }

//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public long getCourseId() {
//        return courseId;
//    }
//
//    public void setCourseId(long courseId) {
//        this.courseId = courseId;
//    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getOutline() {
        return outline;
    }

    public void setOutline(String outline) {
        this.outline = outline;
    }

    @Override
    public String toString()
    {
//        return "id: " + id + ", courseId: " + courseId  +
        return ", level: " + level + ", audience: " + audience
                + ", goal: " + goal + ", outline: " + outline;
    }
}