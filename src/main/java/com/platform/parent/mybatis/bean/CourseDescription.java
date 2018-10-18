package com.platform.parent.mybatis.bean;

/**
 * Created by lsq.
 */
public class CourseDescription {
    private Integer flag;
    private String description;

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CourseDescription(Integer flag, String description) {
        this.flag = flag;
        this.description = description;
    }

    public CourseDescription() {


    }
}