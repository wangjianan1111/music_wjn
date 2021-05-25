package com.bjfu.notation_jh.dao;

import com.bjfu.notation_jh.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author jiahe
 * @create 2020/9/6 15:00
 */
@Mapper
public interface UserMapper {

    /**
     * 根据用户名查询用户信息
     * @param loginId
     * @return
     */
    User selectUserByLoginId(String loginId);

    /**
     * 插入用户信息
     * @param user
     * @return
     */
    int insert(User user);

    /**
     * 根据手机号查询用户信息
     * @param phone
     * @return
     */
    User selectUserByPhone(String phone);

    /**
     * 更新用户信息
     * @param user
     */
    void updateByPrimaryKeySelective(@Param("user") User user);

    /**
     * 根据登录账号查询用户信息
     * @param loginId
     * @return
     */
    User queryByLoginId(String loginId);

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    User queryUserById(@Param("id")String id);
}
