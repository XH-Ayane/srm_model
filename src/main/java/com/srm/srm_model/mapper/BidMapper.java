package com.srm.srm_model.mapper;

import com.srm.srm_model.entity.Bid;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BidMapper {
    // 新增投标
    int insert(Bid bid);

    // 更新投标（招标截止前可修改报价）
    int update(Bid bid);

    // 根据ID查询
    Bid selectById(Long id);

    // 根据招标ID查询所有投标
    List<Bid> selectByTenderId(Long tenderId);

    // 根据招标ID和供应商ID查询
    Bid selectByTenderAndSupplier(
            @Param("tenderId") Long tenderId,
            @Param("supplierId") Long supplierId
    );

    // 根据招标ID查询最低报价的投标
    Bid selectMinPriceByTenderId(Long tenderId);
}