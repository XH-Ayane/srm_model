package com.srm.srm_model.controller;

import com.github.pagehelper.PageInfo;
import com.srm.srm_model.entity.Supplier;
import com.srm.srm_model.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    // 新增供应商
    @PostMapping("/add")
    public Map<String, Object> addSupplier(@RequestBody Supplier supplier) {
        Map<String, Object> result = new HashMap<>();
        try {
            int count = supplierService.addSupplier(supplier);
            if (count > 0) {
                result.put("success", true);
                result.put("message", "供应商添加成功");
            } else {
                result.put("success", false);
                result.put("message", "供应商添加失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "添加失败：" + e.getMessage());
        }
        return result;
    }

    // 分页查询所有供应商
    @GetMapping("/list")
    public Map<String, Object> getAllSuppliers(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String country) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageInfo<Supplier> pageInfo = supplierService.getAllSuppliers(pageNum, pageSize, categoryId, country);
            result.put("success", true);
            result.put("data", pageInfo);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }

    // 获取所有供应商分类
    @GetMapping("/api/supplierCategory")
    public Map<String, Object> getAllCategories(@RequestParam String method) {
        Map<String, Object> result = new HashMap<>();
        if ("getAllCategories".equals(method)) {
            try {
                // 从数据库获取所有供应商分类
                List<String> categoryList = supplierService.getAllCategories();
                List<Map<String, Object>> categories = new ArrayList<>();

                // 转换为前端需要的格式
                for (int i = 0; i < categoryList.size(); i++) {
                    Map<String, Object> category = new HashMap<>();
                    category.put("id", categoryList.get(i)); // 使用实际category值作为ID
                    category.put("name", categoryList.get(i));
                    categories.add(category);
                }

                result.put("success", true);
                result.put("data", categories);
            } catch (Exception e) {
                result.put("success", false);
                result.put("message", "查询失败：" + e.getMessage());
            }
        } else {
            result.put("success", false);
            result.put("message", "不支持的方法");
        }
        return result;
    }

    // 获取所有国家列表
    @GetMapping("/api/getAllCountries")
    public Map<String, Object> getAllCountries(@RequestParam String method) {
        Map<String, Object> result = new HashMap<>();
        if ("getAllCountries".equals(method)) {
            try {
                // 从数据库获取所有国家
                List<String> countryList = supplierService.getAllCountries();
                List<Map<String, Object>> countries = new ArrayList<>();

                // 转换为前端需要的格式
                for (int i = 0; i < countryList.size(); i++) {
                    Map<String, Object> country = new HashMap<>();
                    country.put("id", countryList.get(i)); // 使用实际country值作为ID
                    country.put("name", countryList.get(i));
                    countries.add(country);
                }

                result.put("success", true);
                result.put("data", countryList);
            } catch (Exception e) {
                result.put("success", false);
                result.put("message", "查询失败：" + e.getMessage());
            }
        } else {
            result.put("success", false);
            result.put("message", "不支持的方法");
        }
        return result;
    }

    // 审核供应商
    @PostMapping("/audit")
    public Map<String, Object> auditSupplier(
            @RequestParam Long id,
            @RequestParam Integer status,
            @RequestParam(required = false) String opinion) {
        Map<String, Object> result = new HashMap<>();
        try {
            int count = supplierService.auditSupplier(id, status, opinion);
            if (count > 0) {
                result.put("success", true);
                result.put("message", status == 1 ? "审核通过" : "审核驳回");
            } else {
                result.put("success", false);
                result.put("message", "审核操作失败");
            }
        } catch (IllegalArgumentException e) {
            result.put("success", false);
            result.put("message", e.getMessage()); // 捕获参数校验异常
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "审核失败：" + e.getMessage());
        }
        return result;
    }

    // 更新供应商状态
    @PostMapping("/supplier")
    public Map<String, Object> updateSupplierStatus(
            @RequestParam String method,
            @RequestParam(required = false) Long supplierId,
            @RequestParam String status) {
        Map<String, Object> result = new HashMap<>();
        if ("updateSupplierStatus".equals(method)) {
            // 添加日志输出调试信息
            //System.out.println("updateSupplierStatus called with supplierId: " + supplierId + ", status: " + status);
            try {
                // 检查supplierId是否为null
                if (supplierId == null) {
                    result.put("success", false);
                    result.put("message", "供应商ID不能为空");
                    return result;
                }
                // 创建Supplier对象
                Supplier supplier = new Supplier();
                supplier.setId(supplierId);
                
                // 将字符串状态映射到整数值
                int statusInt;
                switch (status.toLowerCase()) {
                    case "active":
                        statusInt = 1; // 假设1表示激活状态
                        break;
                    case "inactive":
                        statusInt = 0; // 假设0表示非激活状态
                        break;
                    default:
                        // 尝试将状态转换为整数，如果失败会抛出NumberFormatException
                        statusInt = Integer.parseInt(status);
                }
                supplier.setStatus(statusInt);
                
                // 调用现有的updateSupplier方法
                int count = supplierService.updateSupplier(supplier);
                
                if (count > 0) {
                    result.put("success", true);
                    result.put("message", "状态更新成功");
                } else {
                    result.put("success", false);
                    result.put("message", "状态更新失败");
                }
            } catch (NumberFormatException e) {
                result.put("success", false);
                result.put("message", "状态值必须是有效的整数");
            } catch (Exception e) {
                result.put("success", false);
                result.put("message", "状态更新失败：" + e.getMessage());
            }
        } else {
            result.put("success", false);
            result.put("message", "不支持的方法");
        }
        return result;
    }
}