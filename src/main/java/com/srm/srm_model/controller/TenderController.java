package com.srm.srm_model.controller;

import com.github.pagehelper.PageInfo;
import com.srm.srm_model.entity.Tender;
import com.srm.srm_model.service.TenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tender")
public class TenderController {

    @Autowired
    private TenderService tenderService;

    // 新增招标
    @PostMapping("/add")
    public Map<String, Object> addTender(@RequestBody Tender tender) {
        Map<String, Object> result = new HashMap<>();
        try {
            int count = tenderService.addTender(tender);
            if (count > 0) {
                result.put("success", true);
                result.put("message", "招标创建成功");
                result.put("id", tender.getId());
            } else {
                result.put("success", false);
                result.put("message", "招标创建失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "创建失败：" + e.getMessage());
        }
        return result;
    }

    // 分页查询所有招标
    @GetMapping("/list")
    public Map<String, Object> getAllTenders(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageInfo<Tender> pageInfo = tenderService.getAllTenders(pageNum, pageSize);
            result.put("success", true);
            result.put("data", pageInfo);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }

    // 根据ID查询招标
    @GetMapping("/get/{id}")
    public Map<String, Object> getTenderById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Tender tender = tenderService.getTenderById(id);
            if (tender != null) {
                result.put("success", true);
                result.put("data", tender);
            } else {
                result.put("success", false);
                result.put("message", "招标不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }

    // 根据状态查询招标
    @GetMapping("/listByStatus")
    public Map<String, Object> getTendersByStatus(@RequestParam Integer status) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Tender> tenders = tenderService.getTendersByStatus(status);
            result.put("success", true);
            result.put("data", tenders);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
}