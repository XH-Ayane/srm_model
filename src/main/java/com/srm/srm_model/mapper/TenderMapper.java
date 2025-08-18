package com.srm.srm_model.mapper;

import com.srm.srm_model.entity.Tender;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface TenderMapper {
    // 新增招标
    int insert(Tender tender);

    // 更新招标
    int update(Tender tender);

    // 根据ID查询
    Tender selectById(Long id);

    // 根据采购需求ID查询
    Tender selectByRequirementId(Long requirementId);

    // 查询所有招标
    List<Tender> selectAll();

    // 根据状态查询
    List<Tender> selectByStatus(Integer status);
}