package com.srm.srm_model.controller;

import com.github.pagehelper.PageInfo;
import com.srm.srm_model.entity.Tender;
import com.srm.srm_model.service.TenderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tender")
@Api(tags = "招标管理", description = "招标的创建、查询等操作")
public class TenderController {

    @Autowired
    private TenderService tenderService;

    // 新增招标
    @PostMapping("/add")
    @ApiOperation(value = "新增招标", notes = "创建新的招标项目，返回招标ID")
    public Map<String, Object> addTender(
            @ApiParam(value = "招标对象", required = true, example = "{\"name\":\"设备采购招标\",\"requirementId\":1,\"deadline\":\"2024-12-31\"}")
            @RequestBody Tender tender) {
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
    @ApiOperation(value = "分页查询招标", notes = "按页码和每页条数查询所有招标项目")
    public Map<String, Object> getAllTenders(
            @ApiParam(value = "页码，默认1", required = false, example = "1")
            @RequestParam(defaultValue = "1") int pageNum,
            @ApiParam(value = "每页条数，默认10", required = false, example = "10")
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
    @ApiOperation(value = "根据ID查询招标", notes = "通过招标ID获取招标项目详情")
    public Map<String, Object> getTenderById(
            @ApiParam(value = "招标ID", required = true, example = "1")
            @PathVariable Long id) {
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
    @ApiOperation(value = "根据状态查询招标", notes = "通过状态值筛选招标项目（如0=草稿，1=进行中，2=已结束）")
    public Map<String, Object> getTendersByStatus(
            @ApiParam(value = "状态值", required = true, example = "1")
            @RequestParam Integer status) {
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