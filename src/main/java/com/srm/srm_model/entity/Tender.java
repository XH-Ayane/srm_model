package com.srm.srm_model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Tender {
    private Long id;
    private Long requirementId; // 关联采购需求ID
    private String title; // 招标标题
    private String description; // 招标说明
    //private Date startTime; // 开始时间(时间格式出错，已完成替换)
    //private Date endTime; // 结束时间(时间格式出错，已完成替换)
    private Integer status; // 状态：0-未开始，1-进行中，2-已结束
    private Date createTime; // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime; // 开始时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime; // 结束时间

    // 无参构造方法
    public Tender() {
        this.createTime = new Date();
        this.status = 0; // 默认未开始
    }

    // getter和setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(Long requirementId) {
        this.requirementId = requirementId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}