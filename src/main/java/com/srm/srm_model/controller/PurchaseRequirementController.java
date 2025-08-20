package com.srm.srm_model.controller;

import com.github.pagehelper.PageInfo;
import com.srm.srm_model.entity.PurchaseRequirement;
import com.srm.srm_model.service.PurchaseRequirementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/requirement")
@Api(tags = "采购需求管理", description = "采购需求的新增、查询等操作")
public class PurchaseRequirementController {
    @Autowired
    private PurchaseRequirementService requirementService;

    // 新增采购需求
    @PostMapping("/add")
    @ApiOperation(value = "新增采购需求", notes = "创建新的采购需求记录，返回新增记录的ID")
    public Map<String, Object> addRequirement(
            @ApiParam(value = "采购需求对象", required = true, example = "{\"name\":\"原材料采购\",\"quantity\":100}")
            @RequestBody PurchaseRequirement requirement) {
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
    @ApiOperation(value = "分页查询采购需求", notes = "按页码和每页条数查询采购需求列表")
    public Map<String, Object> getAllRequirements(
            @ApiParam(value = "页码，默认1", required = false, example = "1")
            @RequestParam(defaultValue = "1") int pageNum,
            @ApiParam(value = "每页条数，默认10", required = false, example = "10")
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
    @ApiOperation(value = "根据ID查询采购需求", notes = "通过采购需求ID获取详细信息")
    public Map<String, Object> getRequirementById(
            @ApiParam(value = "采购需求ID", required = true, example = "1")
            @PathVariable Long id) {
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