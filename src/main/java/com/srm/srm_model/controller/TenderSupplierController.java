package com.srm.srm_model.controller;

import com.srm.srm_model.entity.TenderSupplier;
import com.srm.srm_model.service.TenderSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tender/supplier")
public class TenderSupplierController {

    @Autowired
    private TenderSupplierService tenderSupplierService;

    // 邀请单个供应商
    @PostMapping("/invite")
    public Map<String, Object> inviteSupplier(@RequestBody TenderSupplier tenderSupplier) {
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
    public Map<String, Object> batchInviteSuppliers(@RequestBody List<TenderSupplier> list) {
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
    public Map<String, Object> getSuppliersByTenderId(@PathVariable Long tenderId) {
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