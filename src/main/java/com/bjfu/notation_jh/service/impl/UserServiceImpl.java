package com.bjfu.notation_jh.service.impl;

import com.bjfu.notation_jh.common.Constants;
import com.bjfu.notation_jh.common.vo.ResultObject;
import com.bjfu.notation_jh.dao.UserMapper;
import com.bjfu.notation_jh.model.User;
import com.bjfu.notation_jh.service.intf.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * @Author john
 * @create 2020/9/6 15:00
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserMapper userMapper;

    /**
     * 根据登录名查询用户
     *
     * @param loginId
     * @return
     */
    @Override
    public User queryUserByloginId(String loginId) {
        return userMapper.selectUserByLoginId(loginId);
    }

    /**
     * 新用户注册
     *
     * @param loginId
     * @param password
     * @return
     */
    @Override
    public ResultObject register(String loginId, String password) {
        ResultObject resultObject = new ResultObject();
        resultObject.setErrorCode(Constants.SUCCESS);

        //验证登录名是否被占用
        User queryUser = userMapper.queryByLoginId(loginId);
        if (queryUser!=null){
            resultObject.setErrorCode("该用户名已被注册，请使用其他名称！");
            return resultObject;
        }

        //1.新增用户
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setLoginId(loginId);
        user.setPassword(password);
        user.setCreateTime(new Date());
        user.setModifyTime(new Date());
        try {
            int insertUserCount = userMapper.insert(user);
        } catch (Exception e) {
            resultObject.setErrorCode("系统异常");
            LOGGER.error("系统异常", e);
        }

        return resultObject;
    }

    @Override
    public User login(String phone) {
        //根据手机号查询用户信息
        User user = userMapper.selectUserByPhone(phone);

        //判断用户是否存在
        if (null != user) {
            //更新最近登录时间
            User updateUser = new User();
            updateUser.setId(user.getId());
            updateUser.setModifyTime(new Date());
            userMapper.updateByPrimaryKeySelective(updateUser);
        } else {
            //1.新增用户
            User insertUser = new User();
            user.setPhone(phone);
            user.setCreateTime(new Date());
            user.setModifyTime(new Date());
            int insertUserCount = userMapper.insert(user);
        }


        return user;
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public User queryUserById(String id) {
        return userMapper.queryUserById(id);
    }
}
