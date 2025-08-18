package com.srm.srm_model.mapper;

import com.srm.srm_model.entity.PurchaseRequirement;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface PurchaseRequirementMapper {
    // 新增采购需求
    int insert(PurchaseRequirement requirement);

    // 更新采购需求
    int update(PurchaseRequirement requirement);

    // 根据ID查询
    PurchaseRequirement selectById(Long id);

    // 查询所有采购需求
    List<PurchaseRequirement> selectAll();
}
