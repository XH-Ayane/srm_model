package com.srm.srm_model.service.impl;

import com.srm.srm_model.entity.TenderSupplier;
import com.srm.srm_model.mapper.TenderSupplierMapper;
import com.srm.srm_model.service.TenderSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TenderSupplierServiceImpl implements TenderSupplierService {

    @Autowired
    private TenderSupplierMapper tenderSupplierMapper;

    @Override
    public int inviteSupplier(TenderSupplier tenderSupplier) {
        return tenderSupplierMapper.insert(tenderSupplier);
    }

    @Override
    public int batchInviteSuppliers(List<TenderSupplier> list) {
        return tenderSupplierMapper.batchInsert(list);
    }

    @Override
    public List<TenderSupplier> getSuppliersByTenderId(Long tenderId) {
        return tenderSupplierMapper.selectByTenderId(tenderId);
    }

    @Override
    public List<TenderSupplier> getTendersBySupplierId(Long supplierId) {
        return tenderSupplierMapper.selectBySupplierId(supplierId);
    }

    @Override
    public int cancelInvitation(Long id) {
        return tenderSupplierMapper.deleteById(id);
    }
}