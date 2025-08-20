package com.srm.srm_model.controller;

import com.srm.srm_model.entity.Bid;
import com.srm.srm_model.service.BidService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bid")
@Api(tags = "投标管理", description = "投标的提交、修改、查询等操作")
public class BidController {

    @Autowired
    private BidService bidService;

    // 提交投标
    @PostMapping("/submit")
    @ApiOperation(value = "提交投标", notes = "供应商提交投标信息，包含投标价格、内容等")
    public Map<String, Object> submitBid(
            @ApiParam(value = "投标对象", required = true, example = "{\"tenderId\":1,\"supplierId\":2,\"price\":9999.99}")
            @RequestBody Bid bid) {
        Map<String, Object> result = new HashMap<>();
        try {
            int count = bidService.submitBid(bid);
            if (count > 0) {
                result.put("success", true);
                result.put("message", "投标提交成功");
            } else {
                result.put("success", false);
                result.put("message", "投标提交失败");
            }
        } catch (IllegalArgumentException e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "提交失败：" + e.getMessage());
        }
        return result;
    }

    // 修改投标
    @PostMapping("/update")
    @ApiOperation(value = "修改投标", notes = "修改已提交的投标信息（需在投标截止前）")
    public Map<String, Object> updateBid(
            @ApiParam(value = "投标对象（含ID）", required = true, example = "{\"id\":1,\"price\":8888.88}")
            @RequestBody Bid bid) {
        Map<String, Object> result = new HashMap<>();
        try {
            int count = bidService.updateBid(bid);
            if (count > 0) {
                result.put("success", true);
                result.put("message", "投标修改成功");
            } else {
                result.put("success", false);
                result.put("message", "投标修改失败");
            }
        } catch (IllegalArgumentException e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "修改失败：" + e.getMessage());
        }
        return result;
    }

    // 查询招标的所有投标
    @GetMapping("/listByTender/{tenderId}")
    @ApiOperation(value = "查询招标的所有投标", notes = "通过招标ID获取该招标下的所有投标记录")
    public Map<String, Object> getBidsByTenderId(
            @ApiParam(value = "招标ID", required = true, example = "1")
            @PathVariable Long tenderId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Bid> bids = bidService.getBidsByTenderId(tenderId);
            result.put("success", true);
            result.put("data", bids);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }

    // 查询最低报价
    @GetMapping("/minPrice/{tenderId}")
    @ApiOperation(value = "查询最低报价投标", notes = "通过招标ID获取该招标下报价最低的投标记录")
    public Map<String, Object> getMinPriceBid(
            @ApiParam(value = "招标ID", required = true, example = "1")
            @PathVariable Long tenderId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Bid minBid = bidService.getMinPriceBid(tenderId);
            result.put("success", true);
            result.put("data", minBid);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
}