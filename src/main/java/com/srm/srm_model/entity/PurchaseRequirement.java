package com.srm.srm_model.entity;

import java.math.BigDecimal;
import java.util.Date;

public class PurchaseRequirement {
    private Long id;
    private String productName; // 产品名称
    private String specification; // 规格
    private Integer quantity; // 数量
    private Date deliveryDate; // 期望交货日期
    private BigDecimal budgetMax; // 最高预算
    private Date createTime; // 创建时间

    // 无参构造方法
    public PurchaseRequirement() {
        this.createTime = new Date();
    }

    // getter和setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public BigDecimal getBudgetMax() {
        return budgetMax;
    }

    public void setBudgetMax(BigDecimal budgetMax) {
        this.budgetMax = budgetMax;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}