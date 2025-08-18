package com.srm.srm_model.service;

import com.github.pagehelper.PageInfo;
import com.srm.srm_model.entity.PurchaseRequirement;
import java.util.List;

public interface PurchaseRequirementService {
    // 新增采购需求
    int addRequirement(PurchaseRequirement requirement);

    // 更新采购需求
    int updateRequirement(PurchaseRequirement requirement);

    // 根据ID查询
    PurchaseRequirement getRequirementById(Long id);

    // 分页查询所有
    PageInfo<PurchaseRequirement> getAllRequirements(int pageNum, int pageSize);
}