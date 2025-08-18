package com.srm.srm_model.service.impl;

import com.srm.srm_model.entity.Bid;
import com.srm.srm_model.entity.Tender;
import com.srm.srm_model.mapper.BidMapper;
import com.srm.srm_model.mapper.TenderMapper;
import com.srm.srm_model.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class BidServiceImpl implements BidService {

    @Autowired
    private BidMapper bidMapper;

    @Autowired
    private TenderMapper tenderMapper;

    @Override
    @Transactional
    public int submitBid(Bid bid) {
        // 1. 校验招标状态（必须是进行中）
        Tender tender = tenderMapper.selectById(bid.getTenderId());
        if (tender == null) {
            throw new IllegalArgumentException("招标不存在");
        }
        if (tender.getStatus() != 1) { // 1表示进行中
            throw new IllegalArgumentException("只有进行中的招标可提交投标");
        }

        // 2. 校验是否已投标
        Bid existingBid = bidMapper.selectByTenderAndSupplier(bid.getTenderId(), bid.getSupplierId());
        if (existingBid != null) {
            throw new IllegalArgumentException("您已提交过投标，可修改报价");
        }

        // 3. 设置投标时间
        bid.setCreateTime(new Date());
        return bidMapper.insert(bid);
    }

    @Override
    @Transactional
    public int updateBid(Bid bid) {
        // 1. 校验招标状态
        Bid existingBid = bidMapper.selectById(bid.getId());
        if (existingBid == null) {
            throw new IllegalArgumentException("投标记录不存在");
        }

        Tender tender = tenderMapper.selectById(existingBid.getTenderId());
        if (tender.getStatus() != 1) {
            throw new IllegalArgumentException("招标已结束或未开始，不能修改投标");
        }

        // 2. 更新投标时间和报价
        bid.setCreateTime(new Date());
        return bidMapper.update(bid);
    }

    @Override
    public Bid getBidById(Long id) {
        return bidMapper.selectById(id);
    }

    @Override
    public List<Bid> getBidsByTenderId(Long tenderId) {
        return bidMapper.selectByTenderId(tenderId);
    }

    @Override
    public boolean hasBid(Long tenderId, Long supplierId) {
        Bid bid = bidMapper.selectByTenderAndSupplier(tenderId, supplierId);
        return bid != null;
    }

    @Override
    public Bid getMinPriceBid(Long tenderId) {
        return bidMapper.selectMinPriceByTenderId(tenderId);
    }
}