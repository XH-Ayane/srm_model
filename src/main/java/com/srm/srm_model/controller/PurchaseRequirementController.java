package com.srm.srm_model.controller;

import com.github.pagehelper.PageInfo;
import com.srm.srm_model.entity.PurchaseRequirement;
import com.srm.srm_model.service.PurchaseRequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/requirement")
public class PurchaseRequirementController {
    @Autowired
    private PurchaseRequirementService requirementService;

    // 新增采购需求
    @PostMapping("/add")
    public Map<String, Object> addRequirement(@RequestBody PurchaseRequirement requirement) {
        Map<String, Object> result = new HashMap<>();
        try {
            int count = requirementService.addRequirement(requirement);
            if (count > 0) {
                result.put("success", true);
                result.put("message", "采购需求创建成功");
                result.put("id", requirement.getId()); // 返回新增的ID
            } else {
                result.put("success", false);
                result.put("message", "采购需求创建失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "创建失败：" + e.getMessage());
        }
        return result;
    }

    // 分页查询所有采购需求
    @GetMapping("/list")
    public Map<String, Object> getAllRequirements(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageInfo<PurchaseRequirement> pageInfo = requirementService.getAllRequirements(pageNum, pageSize);
            result.put("success", true);
            result.put("data", pageInfo);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }

    // 根据ID查询采购需求
    @GetMapping("/get/{id}")
    public Map<String, Object> getRequirementById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            PurchaseRequirement requirement = requirementService.getRequirementById(id);
            if (requirement != null) {
                result.put("success", true);
                result.put("data", requirement);
            } else {
                result.put("success", false);
                result.put("message", "采购需求不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
}