package com.srm.srm_model.service;

import com.github.pagehelper.PageInfo;
import com.srm.srm_model.entity.Tender;
import java.util.List;

public interface TenderService {
    // 新增招标
    int addTender(Tender tender);

    // 更新招标
    int updateTender(Tender tender);

    // 根据ID查询
    Tender getTenderById(Long id);

    // 根据采购需求ID查询
    Tender getTenderByRequirementId(Long requirementId);

    // 分页查询所有招标
    PageInfo<Tender> getAllTenders(int pageNum, int pageSize);

    // 根据状态查询招标
    List<Tender> getTendersByStatus(Integer status);

    // 更新招标状态（自动判断是否开始/结束）
    void updateTenderStatus();
}