package com.srm.srm_model.mapper;

import com.srm.srm_model.entity.Supplier;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper // 标记这是MyBatis接口
public interface SupplierMapper {
    // 新增供应商
    int insert(Supplier supplier);

    // 更新供应商
    int update(Supplier supplier);

    // 根据ID查询
    Supplier selectById(Long id);

    // 查询所有供应商
    List<Supplier> selectAll();

    // 根据状态查询
    List<Supplier> selectByStatus(Integer status);

    // 根据分类ID查询
    List<Supplier> selectByCategoryId(String categoryId);

    // 根据国家查询
    List<Supplier> selectByCountry(String country);

    // 根据分类和国家查询
    List<Supplier> selectByCategoryIdAndCountry(String categoryId, String country);

    // 查询所有供应商分类
    List<String> selectAllCategories();

    /**
     * 查询所有国家
     * @return
     */
    List<String> selectAllCountries();

    // 根据合作状态查询供应商
    List<Supplier> selectByCooperationStatus(Integer cooperationStatus);

    // 根据评级范围查询供应商
    List<Supplier> selectByRatingRange(Integer minRating, Integer maxRating);
}