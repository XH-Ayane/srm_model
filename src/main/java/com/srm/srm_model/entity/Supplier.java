package com.srm.srm_model.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Supplier {
    private Long id;
    private String name;
    private String contactPerson;
    private String phone;
    private String email;
    private String country;
    private String category;
    private Integer status; // 0-待审核，1-已通过，2-已驳回
    private Date createTime;
    private Date updateTime;

    //核心功能增强
    private String qualificationFile; //资质文件路径
    private Integer rating; //评级1-5星
    private String cooperationStatus; // 合作状态: active-活跃, inactive-暂停, terminated-终止
    private String remark; // 备注
    private String auditOpinion; // 审核意见
    private Date auditTime; // 审核时间
}
