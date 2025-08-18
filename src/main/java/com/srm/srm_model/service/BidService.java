package com.srm.srm_model.service;

import com.srm.srm_model.entity.Bid;
import java.util.List;

public interface BidService {
    // 提交投标
    int submitBid(Bid bid);

    // 修改投标报价
    int updateBid(Bid bid);

    // 根据ID查询投标
    Bid getBidById(Long id);

    // 获取招标的所有投标
    List<Bid> getBidsByTenderId(Long tenderId);

    // 检查供应商是否已投标
    boolean hasBid(Long tenderId, Long supplierId);

    // 获取招标的最低报价
    Bid getMinPriceBid(Long tenderId);
}