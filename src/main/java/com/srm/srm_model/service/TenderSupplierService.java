package com.srm.srm_model.service;

import com.srm.srm_model.entity.TenderSupplier;
import java.util.List;

public interface TenderSupplierService {
    // 邀请单个供应商
    int inviteSupplier(TenderSupplier tenderSupplier);

    // 批量邀请供应商
    int batchInviteSuppliers(List<TenderSupplier> list);

    // 查询招标邀请的所有供应商
    List<TenderSupplier> getSuppliersByTenderId(Long tenderId);

    // 查询供应商被邀请的所有招标
    List<TenderSupplier> getTendersBySupplierId(Long supplierId);

    // 取消邀请
    int cancelInvitation(Long id);
}