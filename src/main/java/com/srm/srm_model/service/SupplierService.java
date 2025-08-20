package com.srm.srm_model.service;

import com.github.pagehelper.PageInfo;
import com.srm.srm_model.entity.Supplier;
import java.util.List;

public interface SupplierService {
    // 新增供应商
    int addSupplier(Supplier supplier);

    // 更新供应商
    int updateSupplier(Supplier supplier);

    // 根据ID查询
    Supplier getSupplierById(Long id);

    // 分页查询所有
    PageInfo<Supplier> getAllSuppliers(int pageNum, int pageSize, String categoryId, String country);

    // 根据状态查询
    List<Supplier> getSuppliersByStatus(Integer status);

    // 新增审核相关方法
    /**
     * 审核供应商
     * 
     * @param id      供应商ID
     * @param status  审核结果（1-通过，2-驳回）
     * @param opinion 审核意见
     * @return 影响行数
     */
    int auditSupplier(Long id, Integer status, String opinion);

    /**
     * 获取所有供应商分类
     * 
     * @return 分类列表
     */
    List<String> getAllCategories();

    /**
     * 获取所有国家列表
     * 
     * @return 国家列表
     */
    List<String> getAllCountries();

    // 新增方法
    /**
     * 更新供应商评级
     * @param id 供应商ID
     * @param rating 评级(1-5星)
     * @return 影响行数
     */
    int updateSupplierRating(Long id, Integer rating);

    /**
     * 更新供应商合作状态
     * @param id 供应商ID
     * @param status 合作状态: active-活跃, inactive-暂停, terminated-终止
     * @return 影响行数
     */
    int updateCooperationStatus(Long id, String status);

    /**
     * 上传供应商资质文件
     * @param id 供应商ID
     * @param fileUrl 文件路径
     * @return 影响行数
     */
    int uploadQualificationFile(Long id, String fileUrl);

    /**
     * 根据合作状态查询供应商
     * @param status 合作状态
     * @return 供应商列表
     */
    List<Supplier> getSuppliersByCooperationStatus(String status);

    /**
     * 根据评级查询供应商
     * @param minRating 最小评级
     * @param maxRating 最大评级
     * @return 供应商列表
     */
    List<Supplier> getSuppliersByRating(Integer minRating, Integer maxRating);

}