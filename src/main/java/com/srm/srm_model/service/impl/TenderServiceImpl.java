package com.srm.srm_model.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.srm.srm_model.entity.Tender;
import com.srm.srm_model.mapper.TenderMapper;
import com.srm.srm_model.service.TenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class TenderServiceImpl implements TenderService {

    @Autowired
    private TenderMapper tenderMapper;

    @Override
    public int addTender(Tender tender) {
        // 初始化状态（0-未开始）
        tender.setStatus(0);
        tender.setCreateTime(new Date());
        return tenderMapper.insert(tender);
    }

    @Override
    public int updateTender(Tender tender) {
        return tenderMapper.update(tender);
    }

    @Override
    public Tender getTenderById(Long id) {
        return tenderMapper.selectById(id);
    }

    @Override
    public Tender getTenderByRequirementId(Long requirementId) {
        return tenderMapper.selectByRequirementId(requirementId);
    }

    @Override
    public PageInfo<Tender> getAllTenders(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Tender> tenders = tenderMapper.selectAll();
        return new PageInfo<>(tenders);
    }

    @Override
    public List<Tender> getTendersByStatus(Integer status) {
        return tenderMapper.selectByStatus(status);
    }

    /**
     * 定时任务：自动更新招标状态
     * 每5分钟执行一次（判断招标是否开始或结束）
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    @Override
    public void updateTenderStatus() {
        Date now = new Date();
        // 查询所有未开始和进行中的招标
        List<Tender> tenders = tenderMapper.selectByStatus(0); // 未开始
        tenders.addAll(tenderMapper.selectByStatus(1)); // 进行中

        for (Tender tender : tenders) {
            // 未开始的招标：如果当前时间 >= 开始时间，更新为"进行中"
            if (tender.getStatus() == 0 && now.after(tender.getStartTime())) {
                tender.setStatus(1);
                tenderMapper.update(tender);
            }
            // 进行中的招标：如果当前时间 >= 结束时间，更新为"已结束"
            else if (tender.getStatus() == 1 && now.after(tender.getEndTime())) {
                tender.setStatus(2);
                tenderMapper.update(tender);
            }
        }
    }
}