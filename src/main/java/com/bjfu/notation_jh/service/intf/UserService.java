package com.bjfu.notation_jh.service.intf;

import com.bjfu.notation_jh.common.vo.ResultObject;
import com.bjfu.notation_jh.model.User;
import org.springframework.stereotype.Service;

/**
 * @Author john
 * @create 2020/9/6 15:00
 */
public interface UserService {

    /**
     * 根据登录名查询用户
     * @param loginId
     * @return
     */
    User queryUserByloginId(String loginId);

    /**
     * 新用户注册
     * @param loginId
     * @param password
     * @return
     */
    ResultObject register(String loginId, String password);

    /**
     * 根据手机号登录
     * @param phone
     * @return
     */
    User login(String phone);

    /**
     * 根据id更新用户头像信息
     * @param id
     * @param s
     */
    void updateUser(User user);

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    User queryUserById(String id);
}
