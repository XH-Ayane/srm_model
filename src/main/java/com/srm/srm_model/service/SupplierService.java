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
}