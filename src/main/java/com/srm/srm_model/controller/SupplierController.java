package com.srm.srm_model.controller;

import com.github.pagehelper.PageInfo;
import com.srm.srm_model.common.BusinessException;
import com.srm.srm_model.entity.Supplier;
import com.srm.srm_model.service.SupplierService;
import com.srm.srm_model.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/supplier")
@Api(tags = "供应商管理", description = "供应商的新增、查询、审核、状态更新等操作")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    // 新增供应商
    @PostMapping("/add")
    @ApiOperation(value = "新增供应商", notes = "添加新的供应商信息，包含基本资料、分类等")
    public Result<?> addSupplier(
            @ApiParam(value = "供应商对象", required = true, example = "{\"name\":\"XX公司\",\"category\":\"电子元件\",\"country\":\"中国\"}")
            @RequestBody Supplier supplier) {
        try {
            int count = supplierService.addSupplier(supplier);
            if (count > 0) {
                return Result.success("供应商添加成功", null);
            } else {
                return Result.fail("供应商添加失败");
            }
        } catch (Exception e) {
            return Result.fail("添加失败：" + e.getMessage());
        }
    }

    // 分页查询所有供应商
    @GetMapping("/list")
    @ApiOperation(value = "分页查询供应商", notes = "按页码、每页条数、分类、国家筛选供应商")
    public Result<PageInfo<Supplier>> getAllSuppliers(
            @ApiParam(value = "页码，默认1", required = false, example = "1")
            @RequestParam(defaultValue = "1") int pageNum,
            @ApiParam(value = "每页条数，默认10", required = false, example = "10")
            @RequestParam(defaultValue = "10") int pageSize,
            @ApiParam(value = "分类ID（可选）", required = false, example = "电子元件")
            @RequestParam(required = false) String categoryId,
            @ApiParam(value = "国家（可选）", required = false, example = "中国")
            @RequestParam(required = false) String country) {
        try {
            PageInfo<Supplier> pageInfo = supplierService.getAllSuppliers(pageNum, pageSize, categoryId, country);
            return Result.success(pageInfo);
        } catch (Exception e) {
            return Result.fail("查询失败：" + e.getMessage());
        }
    }

    // 获取所有供应商分类
    @GetMapping("/api/supplierCategory")
    @ApiOperation(value = "获取所有供应商分类", notes = "查询系统中所有供应商分类（需指定method=getAllCategories）")
    public Result<List<Map<String, Object>>> getAllCategories(
            @ApiParam(value = "方法名，固定为getAllCategories", required = true, example = "getAllCategories")
            @RequestParam String method) {
        if ("getAllCategories".equals(method)) {
            try {
                List<String> categoryList = supplierService.getAllCategories();
                List<Map<String, Object>> categories = new ArrayList<>();
                for (int i = 0; i < categoryList.size(); i++) {
                    Map<String, Object> category = new HashMap<>();
                    category.put("id", categoryList.get(i));
                    category.put("name", categoryList.get(i));
                    categories.add(category);
                }
                return Result.success(categories);
            } catch (Exception e) {
                return Result.fail("查询失败：" + e.getMessage());
            }
        } else {
            return Result.fail("不支持的方法");
        }
    }

    // 获取所有国家列表
    @GetMapping("/api/getAllCountries")
    @ApiOperation(value = "获取所有国家列表", notes = "查询系统中所有供应商所在国家（需指定method=getAllCountries）")
    public Result<List<String>> getAllCountries(
            @ApiParam(value = "方法名，固定为getAllCountries", required = true, example = "getAllCountries")
            @RequestParam String method) {
        if ("getAllCountries".equals(method)) {
            try {
                List<String> countryList = supplierService.getAllCountries();
                return Result.success(countryList);
            } catch (Exception e) {
                return Result.fail("查询失败：" + e.getMessage());
            }
        } else {
            return Result.fail("不支持的方法");
        }
    }

    // 审核供应商
    @PostMapping("/audit")
    @ApiOperation(value = "审核供应商", notes = "审核供应商资质，设置审核状态（1=通过，0=驳回）及意见")
    public Result<?> auditSupplier(
            @ApiParam(value = "供应商ID", required = true, example = "1")
            @RequestParam Long id,
            @ApiParam(value = "审核状态（1=通过，0=驳回）", required = true, example = "1")
            @RequestParam Integer status,
            @ApiParam(value = "审核意见（可选）", required = false, example = "资质符合要求")
            @RequestParam(required = false) String opinion) {
        try {
            int count = supplierService.auditSupplier(id, status, opinion);
            if (count > 0) {
                return Result.success(status == 1 ? "审核通过" : "审核驳回");
            } else {
                return Result.fail("审核操作失败");
            }
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            return Result.fail("审核失败：" + e.getMessage());
        }
    }

    // 更新供应商状态
    @PostMapping("/supplier")
    @ApiOperation(value = "更新供应商状态", notes = "修改供应商的激活状态（需指定method=updateSupplierStatus）")
    public Result<?> updateSupplierStatus(
            @ApiParam(value = "方法名，固定为updateSupplierStatus", required = true, example = "updateSupplierStatus")
            @RequestParam String method,
            @ApiParam(value = "供应商ID", required = true, example = "1")
            @RequestParam(required = false) Long supplierId,
            @ApiParam(value = "状态（active=激活，inactive=未激活或整数）", required = true, example = "active")
            @RequestParam String status) {
        if ("updateSupplierStatus".equals(method)) {
            try {
                if (supplierId == null) {
                    return Result.fail("供应商ID不能为空");
                }
                Supplier supplier = new Supplier();
                supplier.setId(supplierId);
                int statusInt;
                switch (status.toLowerCase()) {
                    case "active":
                        statusInt = 1;
                        break;
                    case "inactive":
                        statusInt = 0;
                        break;
                    default:
                        statusInt = Integer.parseInt(status);
                }
                supplier.setStatus(statusInt);
                int count = supplierService.updateSupplier(supplier);
                if (count > 0) {
                    return Result.success("状态更新成功");
                } else {
                    return Result.fail("状态更新失败");
                }
            } catch (NumberFormatException e) {
                return Result.fail("状态值必须是有效的整数");
            } catch (Exception e) {
                return Result.fail("状态更新失败：" + e.getMessage());
            }
        } else {
            return Result.fail("不支持的方法");
        }
    }
    @PostMapping("/rating/update")
    @ApiOperation(value = "更新供应商评级", notes = "设置供应商的评级(1-5星)")
    public Result<?> updateSupplierRating(
            @ApiParam(value = "供应商ID", required = true, example = "1")
            @RequestParam Long id,
            @ApiParam(value = "评级(1-5星)", required = true, example = "4")
            @RequestParam Integer rating) {
        try {
            int count = supplierService.updateSupplierRating(id, rating);
            if (count > 0) {
                return Result.success("评级更新成功");
            } else {
                return Result.fail("评级更新失败");
            }
        } catch (BusinessException e) {
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            return Result.fail("更新失败：" + e.getMessage());
        }
    }

    @PostMapping("/cooperation/status/update")
    @ApiOperation(value = "更新合作状态", notes = "设置供应商的合作状态(active-活跃, inactive-暂停, terminated-终止)")
    public Result<?> updateCooperationStatus(
            @ApiParam(value = "供应商ID", required = true, example = "1")
            @RequestParam Long id,
            @ApiParam(value = "合作状态", required = true, example = "active")
            @RequestParam String status) {
        try {
            int count = supplierService.updateCooperationStatus(id, status);
            if (count > 0) {
                return Result.success("合作状态更新成功");
            } else {
                return Result.fail("合作状态更新失败");
            }
        } catch (BusinessException e) {
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            return Result.fail("更新失败：" + e.getMessage());
        }
    }

    @PostMapping("/qualification/upload")
    @ApiOperation(value = "上传资质文件", notes = "上传供应商资质文件并更新路径")
    public Result<?> uploadQualificationFile(
            @ApiParam(value = "供应商ID", required = true, example = "1")
            @RequestParam Long id,
            @ApiParam(value = "文件路径", required = true, example = "/uploads/qualifications/supplier1.pdf")
            @RequestParam String fileUrl) {
        try {
            int count = supplierService.uploadQualificationFile(id, fileUrl);
            if (count > 0) {
                return Result.success("资质文件上传成功");
            } else {
                return Result.fail("资质文件上传失败");
            }
        } catch (BusinessException e) {
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            return Result.fail("上传失败：" + e.getMessage());
        }
    }

    @GetMapping("/list/cooperationStatus")
    @ApiOperation(value = "按合作状态查询", notes = "根据合作状态查询供应商列表")
    public Result<List<Supplier>> getSuppliersByCooperationStatus(
            @ApiParam(value = "合作状态", required = true, example = "active")
            @RequestParam String status) {
        try {
            List<Supplier> suppliers = supplierService.getSuppliersByCooperationStatus(status);
            return Result.success(suppliers);
        } catch (BusinessException e) {
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            return Result.fail("查询失败：" + e.getMessage());
        }
    }

    @GetMapping("/list/rating")
    @ApiOperation(value = "按评级查询", notes = "根据评级范围查询供应商列表")
    public Result<List<Supplier>> getSuppliersByRating(
            @ApiParam(value = "最小评级", required = true, example = "3")
            @RequestParam Integer minRating,
            @ApiParam(value = "最大评级", required = true, example = "5")
            @RequestParam Integer maxRating) {
        try {
            List<Supplier> suppliers = supplierService.getSuppliersByRating(minRating, maxRating);
            return Result.success(suppliers);
        } catch (BusinessException e) {
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            return Result.fail("查询失败：" + e.getMessage());
        }
    }
}
