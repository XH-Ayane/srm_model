package com.srm.srm_model.controller;

import com.srm.srm_model.entity.Bid;
import com.srm.srm_model.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bid")
public class BidController {

    @Autowired
    private BidService bidService;

    // 提交投标
    @PostMapping("/submit")
    public Map<String, Object> submitBid(@RequestBody Bid bid) {
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
    public Map<String, Object> updateBid(@RequestBody Bid bid) {
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
    public Map<String, Object> getBidsByTenderId(@PathVariable Long tenderId) {
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
    public Map<String, Object> getMinPriceBid(@PathVariable Long tenderId) {
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