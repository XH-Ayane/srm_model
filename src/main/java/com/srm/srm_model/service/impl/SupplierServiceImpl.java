package com.srm.srm_model.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.srm.srm_model.common.BusinessException;
import com.srm.srm_model.entity.Supplier;
import com.srm.srm_model.mapper.SupplierMapper;
import com.srm.srm_model.service.SupplierService;
import com.srm.srm_model.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierMapper supplierMapper;

    @Autowired
    private RedisUtil redisUtil;

    // 缓存key前缀
    private static final String SUPPLIER_CACHE_KEY = "supplier:";
    private static final String SUPPLIER_LIST_CACHE_KEY = "supplier:list:status:";
    //缓存常量更新
    private static final String SUPPLIER_LIST_COOP_STATUS_KEY = "supplier:list:coopStatus:";
    private static final String SUPPLIER_LIST_RATING_KEY = "supplier:list:rating:";


    @Override
    public Supplier getSupplierById(Long id) {
        // 缓存查询
        String key = SUPPLIER_CACHE_KEY + id;
        Supplier supplier = (Supplier) redisUtil.get(key);

        // 缓存不足查数据库
        if (supplier == null) {
            supplier = supplierMapper.selectById(id);
            // 写入缓存
            if (supplier != null) {
                redisUtil.set(key, supplier, 10);
            } else {
                // 供应商不存在时抛出业务异常
                throw new BusinessException("未找到ID为" + id + "的供应商");
            }
        }

        return supplier;
    }

    @Override
    public int addSupplier(Supplier supplier) {
        // 校验供应商信息是否完整
        if (supplier.getName() == null || supplier.getName().trim().isEmpty()) {
            throw new BusinessException("供应商名称不能为空");
        }

        // 新供应商默认状态为待审核
        supplier.setStatus(0);
        // 确保时间正确设置
        supplier.setCreateTime(new Date());
        supplier.setUpdateTime(new Date());
        return supplierMapper.insert(supplier);
    }

    @Override
    public int updateSupplier(Supplier supplier) {
        if (supplier.getId() == null) {
            throw new BusinessException("供应商ID不能为空");
        }

        // 检查供应商是否存在
        Supplier existingSupplier = supplierMapper.selectById(supplier.getId());
        if (existingSupplier == null) {
            throw new BusinessException("要更新的供应商不存在");
        }

        supplier.setUpdateTime(new Date());
        int result = supplierMapper.update(supplier);
        // 更新后删除对应缓存，保证数据一致性
        redisUtil.delete(SUPPLIER_CACHE_KEY + supplier.getId());
        return result;
    }

    @Override
    public PageInfo<Supplier> getAllSuppliers(int pageNum, int pageSize, String categoryId, String country) {
        if (pageNum < 1 || pageSize < 1) {
            throw new BusinessException("页码和每页条数必须为正整数");
        }

        // 开启分页
        PageHelper.startPage(pageNum, pageSize);
        List<Supplier> suppliers;
        if (categoryId != null && country != null) {
            suppliers = supplierMapper.selectByCategoryIdAndCountry(categoryId, country);
        } else if (categoryId != null) {
            suppliers = supplierMapper.selectByCategoryId(categoryId);
        } else if (country != null) {
            suppliers = supplierMapper.selectByCountry(country);
        } else {
            suppliers = supplierMapper.selectAll();
        }
        return new PageInfo<>(suppliers);
    }

    @Override
    public List<Supplier> getSuppliersByStatus(Integer status) {
        if (status == null) {
            throw new BusinessException("供应商状态不能为空");
        }

        // 1. 先查缓存
        String key = SUPPLIER_LIST_CACHE_KEY + status;
        List<Supplier> suppliers = (List<Supplier>) redisUtil.get(key);

        // 2. 缓存不存在则查数据库
        if (suppliers == null) {
            suppliers = supplierMapper.selectByStatus(status);
            // 3. 存入缓存（设置5分钟过期）
            if (suppliers != null) {
                redisUtil.set(key, suppliers, 5);
            }
        }
        return suppliers;
    }

    @Override
    public int auditSupplier(Long id, Integer status, String opinion) {
        // 1. 校验状态合法性（只能是1通过或2驳回）
        if (status != 1 && status != 2) {
            throw new BusinessException("审核状态只能是1（通过）或2（驳回）");
        }

        // 2. 查询供应商是否存在
        Supplier supplier = supplierMapper.selectById(id);
        if (supplier == null) {
            throw new BusinessException("供应商不存在");
        }

        // 3. 只能审核"待审核"状态的供应商
        if (supplier.getStatus() != 0) {
            throw new BusinessException("只有待审核状态的供应商可进行审核");
        }

        // 4. 更新供应商状态
        supplier.setStatus(status);
        supplier.setUpdateTime(new Date());
        int result = supplierMapper.update(supplier);

        // 缓存删除结果语句
        redisUtil.delete(SUPPLIER_CACHE_KEY + id);
        redisUtil.delete(SUPPLIER_LIST_CACHE_KEY + 0); // 删除待审核列表缓存
        redisUtil.delete(SUPPLIER_LIST_CACHE_KEY + status); // 删除结果状态列表缓存

        // 5. 记录审核意见（后续可扩展到审核记录表）
        System.out.println("审核意见：" + opinion); // 临时打印，后续优化

        return result;
    }

    @Override
    public List<String> getAllCategories() {
        // 1. 缓存key
        String key = "supplier:categories";

        // 2. 先查缓存
        Object cacheObj = redisUtil.get(key);
        List<String> categories = null;

        // 3. 安全地转换缓存数据类型
        if (cacheObj instanceof List) {
            categories = (List<String>) cacheObj;
        }

        // 4. 缓存不存在或类型错误则查数据库
        if (categories == null) {
            categories = supplierMapper.selectAllCategories();
            // 5. 存入缓存（设置5分钟过期）
            if (categories != null) {
                redisUtil.set(key, categories, 5);
            } else {
                throw new BusinessException("获取供应商分类失败");
            }
        }

        return categories;
    }

    @Override
    public List<String> getAllCountries() {
        // 1. 缓存key
        String key = "supplier:countries";

        // 2. 先查缓存
        Object cacheObj = redisUtil.get(key);
        List<String> countries = null;

        // 3. 安全地转换缓存数据类型
        if (cacheObj instanceof List) {
            countries = (List<String>) cacheObj;
        }

        // 4. 缓存不存在或类型错误则查数据库
        if (countries == null) {
            countries = supplierMapper.selectAllCountries();
            // 5. 存入缓存（设置5分钟过期）
            if (countries != null) {
                redisUtil.set(key, countries, 5);
            } else {
                throw new BusinessException("获取国家列表失败");
            }
        }

        return countries;
    }

    @Override
    public int updateSupplierRating(Long id, Integer rating) {
        //评级范围校验
        if (rating < 1 || rating > 5){
            throw new BusinessException("评级必须在1-5星之间");
        }

        //供应商存在性检查
        Supplier supplier = getSupplierById(id);

        //评级更新
        supplier.setRating(rating);
        supplier.setUpdateTime(new Date());
        int result = supplierMapper.update(supplier);

        //缓存更新
        redisUtil.delete(SUPPLIER_CACHE_KEY + id);
        //缓存事务化，清除多余缓存
        redisUtil.delete(SUPPLIER_LIST_RATING_KEY + "1-5");
        return result;
    }

    @Override
    public int updateCooperationStatus(Long id, String status) {
        //校验状态合法性
        if (!"active".equals(status) && !"inactive".equals(status) && !"terminated".equals(status)) {
            throw new BusinessException("合作状态只能是active、inactive或terminated");
        }

        //检查供应商是否存在
        Supplier supplier = getSupplierById(id);
        //更新合作状态
        supplier.setCooperationStatus(status);
        supplier.setUpdateTime(new Date());
        int result =  supplierMapper.update(supplier);

        redisUtil.delete(SUPPLIER_CACHE_KEY + id);
        // 删除可能影响的列表缓存
        redisUtil.delete(SUPPLIER_LIST_COOP_STATUS_KEY + supplier.getCooperationStatus());

        return result;
    }

    @Override
    public int uploadQualificationFile(Long id, String fileUrl) {
        if (fileUrl == null || fileUrl.trim().isEmpty()) {
            throw new BusinessException("文件路径不能为空");
        }

        // 检查供应商是否存在
        Supplier supplier = getSupplierById(id);

        // 更新资质文件路径
        supplier.setQualificationFile(fileUrl);
        supplier.setUpdateTime(new Date());
        int result = supplierMapper.update(supplier);

        // 更新缓存
        redisUtil.delete(SUPPLIER_CACHE_KEY + id);

        return result;
    }

    @Override
    public List<Supplier> getSuppliersByCooperationStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new BusinessException("合作状态不能为空");
        }

        // 检查状态合法性
        if (!"active".equals(status) && !"inactive".equals(status) && !"terminated".equals(status)) {
            throw new BusinessException("无效的合作状态");
        }

        // 查询缓存
        String key = SUPPLIER_LIST_COOP_STATUS_KEY + status;
        List<Supplier> suppliers = (List<Supplier>) redisUtil.get(key);

        // 缓存不存在则查询数据库
        if (suppliers == null) {
            suppliers = supplierMapper.selectByCooperationStatus(Integer.valueOf(status));
            // 存入缓存(5分钟)
            if (suppliers != null) {
                redisUtil.set(key, suppliers, 5);
            }
        }

        return suppliers;
    }

    @Override
    public List<Supplier> getSuppliersByRating(Integer minRating, Integer maxRating) {
        // 校验评级范围
        if (minRating < 1 || maxRating > 5 || minRating > maxRating) {
            throw new BusinessException("评级范围无效");
        }

        // 查询缓存
        String key = SUPPLIER_LIST_RATING_KEY + minRating + "-" + maxRating;
        List<Supplier> suppliers = (List<Supplier>) redisUtil.get(key);

        // 缓存不存在则查询数据库
        if (suppliers == null) {
            suppliers = supplierMapper.selectByRatingRange(minRating, maxRating);
            // 存入缓存(5分钟)
            if (suppliers != null) {
                redisUtil.set(key, suppliers, 5);
            }
        }

        return suppliers;
    }
}
