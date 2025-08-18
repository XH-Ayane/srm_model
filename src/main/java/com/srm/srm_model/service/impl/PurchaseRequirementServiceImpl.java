package com.srm.srm_model.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.srm.srm_model.entity.PurchaseRequirement;
import com.srm.srm_model.mapper.PurchaseRequirementMapper;
import com.srm.srm_model.service.PurchaseRequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class PurchaseRequirementServiceImpl implements PurchaseRequirementService {

    @Autowired
    private PurchaseRequirementMapper requirementMapper;

    @Override
    public int addRequirement(PurchaseRequirement requirement) {
        requirement.setCreateTime(new Date());
        return requirementMapper.insert(requirement);
    }

    @Override
    public int updateRequirement(PurchaseRequirement requirement) {
        return requirementMapper.update(requirement);
    }

    @Override
    public PurchaseRequirement getRequirementById(Long id) {
        return requirementMapper.selectById(id);
    }

    @Override
    public PageInfo<PurchaseRequirement> getAllRequirements(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<PurchaseRequirement> requirements = requirementMapper.selectAll();
        return new PageInfo<>(requirements);
    }
}
