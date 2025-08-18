package com.srm.srm_model.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.srm.srm_model.entity.Supplier;
import com.srm.srm_model.mapper.SupplierMapper;
import com.srm.srm_model.service.SupplierService;
import com.srm.srm_model.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
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
            }
        }

        return supplier;
    }

    @Override
    public int addSupplier(Supplier supplier) {
        // 新供应商默认状态为待审核
        supplier.setStatus(0);
        // 确保时间正确设置
        supplier.setCreateTime(new Date());
        supplier.setUpdateTime(new Date());
        return supplierMapper.insert(supplier);
    }

    @Override
    public int updateSupplier(Supplier supplier) {
        supplier.setUpdateTime(new Date());
        int result = supplierMapper.update(supplier);
        // 更新后删除对应缓存，保证数据一致性
        redisUtil.delete(SUPPLIER_CACHE_KEY + supplier.getId());
        return result;
    }

    @Override
    public PageInfo<Supplier> getAllSuppliers(int pageNum, int pageSize, String categoryId, String country) {
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
            throw new IllegalArgumentException("审核状态只能是1（通过）或2（驳回）");
        }

        // 2. 查询供应商是否存在
        Supplier supplier = supplierMapper.selectById(id);
        if (supplier == null) {
            throw new IllegalArgumentException("供应商不存在");
        }

        // 3. 只能审核"待审核"状态的供应商
        if (supplier.getStatus() != 0) {
            throw new IllegalArgumentException("只有待审核状态的供应商可进行审核");
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
            }
        }

        return countries;
    }
}