package com.srm.srm_model.controller;

import com.srm.srm_model.entity.TenderSupplier;
import com.srm.srm_model.service.TenderSupplierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tender/supplier")
@Api(tags = "招标供应商邀请", description = "向供应商发送招标邀请的相关操作")
public class TenderSupplierController {

    @Autowired
    private TenderSupplierService tenderSupplierService;

    // 邀请单个供应商
    @PostMapping("/invite")
    @ApiOperation(value = "邀请单个供应商", notes = "向指定供应商发送招标邀请")
    public Map<String, Object> inviteSupplier(
            @ApiParam(value = "招标-供应商关联对象", required = true, example = "{\"tenderId\":1,\"supplierId\":2}")
            @RequestBody TenderSupplier tenderSupplier) {
        Map<String, Object> result = new HashMap<>();
        try {
            int count = tenderSupplierService.inviteSupplier(tenderSupplier);
            if (count > 0) {
                result.put("success", true);
                result.put("message", "邀请成功");
            } else {
                result.put("success", false);
                result.put("message", "邀请失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "邀请失败：" + e.getMessage());
        }
        return result;
    }

    // 批量邀请供应商
    @PostMapping("/batchInvite")
    @ApiOperation(value = "批量邀请供应商", notes = "向多个供应商批量发送同一招标的邀请")
    public Map<String, Object> batchInviteSuppliers(
            @ApiParam(value = "招标-供应商关联对象列表", required = true, example = "[{\"tenderId\":1,\"supplierId\":2},{\"tenderId\":1,\"supplierId\":3}]")
            @RequestBody List<TenderSupplier> list) {
        Map<String, Object> result = new HashMap<>();
        try {
            int count = tenderSupplierService.batchInviteSuppliers(list);
            if (count > 0) {
                result.put("success", true);
                result.put("message", "批量邀请成功，共邀请" + count + "家供应商");
            } else {
                result.put("success", false);
                result.put("message", "批量邀请失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "批量邀请失败：" + e.getMessage());
        }
        return result;
    }

    // 查询招标邀请的供应商
    @GetMapping("/listByTender/{tenderId}")
    @ApiOperation(value = "查询招标邀请的供应商", notes = "通过招标ID获取该招标已邀请的所有供应商")
    public Map<String, Object> getSuppliersByTenderId(
            @ApiParam(value = "招标ID", required = true, example = "1")
            @PathVariable Long tenderId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<TenderSupplier> list = tenderSupplierService.getSuppliersByTenderId(tenderId);
            result.put("success", true);
            result.put("data", list);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
}