package com.srm.srm_model.mapper;

import com.srm.srm_model.entity.TenderSupplier;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface TenderSupplierMapper {
    // 新增关联（邀请供应商）
    int insert(TenderSupplier tenderSupplier);

    // 批量新增关联
    int batchInsert(List<TenderSupplier> list);

    // 根据招标ID查询关联的供应商
    List<TenderSupplier> selectByTenderId(Long tenderId);

    // 根据供应商ID查询关联的招标
    List<TenderSupplier> selectBySupplierId(Long supplierId);

    // 删除关联
    int deleteById(Long id);
}