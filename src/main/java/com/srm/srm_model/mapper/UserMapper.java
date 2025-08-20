package com.srm.srm_model.mapper;

import com.srm.srm_model.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户Mapper接口
 * 提供用户数据访问操作
 *
 * @author [作者名]
 * @date 2023-xx-xx
 */
@Mapper
public interface UserMapper {
    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户对象
     */
    User selectByUsername(String username);

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户对象
     */
    User selectById(Long id);

    /**
     * 查询所有用户
     *
     * @return 用户列表
     */
    List<User> selectAll();

    /**
     * 插入用户
     *
     * @param user 用户对象
     * @return 受影响的行数
     */
    int insert(User user);

    /**
     * 更新用户
     *
     * @param user 用户对象
     * @return 受影响的行数
     */
    int update(User user);

    /**
     * 根据ID删除用户
     *
     * @param id 用户ID
     * @return 受影响的行数
     */
    int deleteById(Long id);
}