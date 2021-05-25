package com.bjfu.notation_jh.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.bjfu.notation_jh.common.Constants;
import com.bjfu.notation_jh.common.request.UserRequest;
import com.bjfu.notation_jh.common.response.Result;
import com.bjfu.notation_jh.common.vo.ResultObject;
import com.bjfu.notation_jh.model.User;
import com.bjfu.notation_jh.service.intf.RedisService;
import com.bjfu.notation_jh.service.intf.UserService;
import com.bjfu.notation_jh.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * @Author jiahe
 * @create 2020/9/6 15:00
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);


    @Resource
    private UserService userService;

    @Resource
    private RedisService redisService;

//    @Value("G:/000BJFU/imagesface/")
    @Value("/profile/imagesface/")
    private String path;

//    @Value("http://localhost:8082/imagesface/")
    @Value("/profile/imagesface/")
    private String localPath;


    /**
     * 判断用户是否登录（session是否失效）
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/isLoginIn")
    @ResponseBody
    public Result isLoginIn(HttpServletRequest request) {
        Result result = new Result(true);
        User user = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        if (user == null){
            //301--session已过期，重新登录
            result.setStatusCode(301);
        }else {
            //201--session未过期
            result.setStatusCode(201);
        }
        return result;
    }


    /**
     * 注册：校验用户名是否已经存在
     *
     * @param request
     * @param loginId
     * @return JSON对象{"errorMessage":"OK"}
     */
    @RequestMapping(value = "/checkLoginId")
    @ResponseBody
    public Object checkPhone(HttpServletRequest request,
                             @RequestParam(value = "loginId", required = true) String loginId) {
        Map<String, Object> retMap = new HashMap<String, Object>();


        User user = userService.queryUserByloginId(loginId);

        //判断用户是否存在
        if (null != user) {
            retMap.put(Constants.ERROR_MESSAGE, "该登录名已被使用");
            return retMap;
        }

        retMap.put(Constants.ERROR_MESSAGE, Constants.OK);

        return retMap;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Object register(HttpServletRequest request,
                           @RequestParam(value = "loginId", required = true) String loginId,
                           @RequestParam(value = "password", required = true) String password) {
        Map<String, Object> retMap = new HashMap<String, Object>();

        //用户注册【1.新增用户信息 2.新增帐户信息】（手机号，登录密码） -> 返回业务处理对象ResultObject
        ResultObject resultObject = userService.register(loginId, password);

        //判断是否注册成功
        if (StringUtils.equals(resultObject.getErrorCode(), Constants.SUCCESS)) {
            User user = userService.queryUserByloginId(loginId);
            //将用户的信息存放到session中
//            HttpSession session = request.getSession();
//            session.setAttribute("user", user);
            request.getSession().setAttribute(Constants.SESSION_USER, user);
            request.getSession().setMaxInactiveInterval(120*60);
            resultObject.setUser(user);
            retMap.put(Constants.GLOBALUSER, user);
            retMap.put(Constants.ERROR_MESSAGE, Constants.SUCCESSCODE);
        } else {
            retMap.put(Constants.ERROR_MESSAGE, resultObject.getErrorCode());
            return retMap;
        }
        return retMap;
    }


    /**
     * 使用loginId登录
     *
     * @param request
     * @param loginId
     * @param password
     * @return
     */
    @RequestMapping(value = "/loginByLoginId")
    @ResponseBody
    public Object loginByLoginId(HttpServletRequest request,
                                 @RequestParam(value = "loginId", required = true) String loginId,
                                 @RequestParam(value = "password", required = true) String password) {
        Map<String, Object> retMap = new HashMap<String, Object>();

        //password是否需要转换md5 //TODO
        User user = userService.queryUserByloginId(loginId);

        if (user == null) {
            retMap.put(Constants.ERROR_MESSAGE, "该用户不存在");
            return retMap;
        }

        if (StringUtils.equals(password, user.getPassword())) {
            //将用户的信息存放到session中
            request.getSession().setAttribute(Constants.SESSION_USER, userService.queryUserByloginId(loginId));
            request.getSession().setMaxInactiveInterval(120*60);
            retMap.put(Constants.GLOBALUSER, user);
            retMap.put(Constants.ERROR_MESSAGE, Constants.SUCCESSCODE);
            return retMap;
        } else {
            retMap.put(Constants.ERROR_MESSAGE, "登录失败");
            return retMap;
        }
    }

    /**
     * 使用手机号登录用户
     *
     * @param request
     * @param phone
     * @param messageCode
     * @return
     */
    @RequestMapping(value = "/loginByPhone")
    @ResponseBody
    public Object loginloginByPhone(HttpServletRequest request,
                                    @RequestParam(value = "phone", required = true) String phone,
                                    @RequestParam(value = "messageCode", required = true) String messageCode) {
        Map<String, Object> retMap = new HashMap<String, Object>();


        //获取redis中手机号对应的短信验证码
        String redisMessageCode = redisService.get(phone);

        //判断短信验证码是否正确
        if (StringUtils.equals(messageCode, redisMessageCode)) {

            //登录的业务【根据手机号查询用户登录信息，并更新时间，如果查询为空就插入新数据】 -> 返回User
            try {
                User user = userService.login(phone);
                //将用户的信息存放到session中
                request.getSession().setAttribute(Constants.SESSION_USER, user);
                request.getSession().setMaxInactiveInterval(120*60);
                retMap.put(Constants.GLOBALUSER, user);
                retMap.put(Constants.ERROR_MESSAGE, Constants.OK);
            } catch (Exception e) {
                retMap.put(Constants.ERROR_MESSAGE, Constants.FAIL);
            }

        } else {
            retMap.put(Constants.ERROR_MESSAGE, "请输入正确的短信验证码");
            return retMap;
        }


        return retMap;
    }

    @RequestMapping(value = "/messageCode")
    public @ResponseBody
    Object messageCode(HttpServletRequest request,
                       @RequestParam(value = "phone", required = true) String phone) throws Exception {
        Map<String, Object> retMap = new HashMap<String, Object>();

        //生成一个随机数字
        String messageCode = this.getRandomNumber(4);

        //发送短信的内容=短信签名+短信正文(包含：一个随机数字)
        String content = "【美音合成】您的验证码是：" + messageCode;

        //准备发送短信的参数
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("appkey", "dd39489ee52a85359edbe3b51b");
        paramMap.put("mobile", phone);
        paramMap.put("content", content);

        //发送短信：调用互联网接口发送
//        String jsonString = HttpClientUtils.doPost("https://way.jd.com/kaixintong/kaixintong", paramMap);
        String jsonString = "{\"code\":\"10000\",\"charge\":false,\"remain\":0,\"msg\":\"查询成功\",\"result\":\"<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\" ?><returnsms>\\n <returnstatus>Success</returnstatus>\\n <message>ok</message>\\n <remainpoint>-616780</remainpoint>\\n <taskID>78950259</taskID>\\n <successCounts>1</successCounts></returnsms>\"}";

        //解析json格式的字符串
        //将json字符串转换为json对象
        JSONObject jsonObject = JSONObject.parseObject(jsonString);

        //获取通信标识
        String code = jsonObject.getString("code");

        //判断通信结果
        if (StringUtils.equals("10000", code)) {

            //获取result的值,result是一个xml格式的字符串
            String result = jsonObject.getString("result");

            //使用dom4j+xpath解析xml格式的字符串
            //将xml格式的字符串转换为dom对象
            Document document = DocumentHelper.parseText(result);

            //获取returnstatus
            Node node = document.selectSingleNode("//returnstatus");

            //获取节点对象的文本内容
            String returnStatus = node.getText();

            //判断短信是否发送成功
            if (StringUtils.equals(returnStatus, "Success")) {

                retMap.put(Constants.ERROR_MESSAGE, Constants.OK);

                //将生成的验证码存放到redis缓存中
                redisService.put(phone, messageCode);

                retMap.put("messageCode", messageCode);


            } else {
                retMap.put(Constants.ERROR_MESSAGE, "短信发送失败，请稍后重试...");
                return retMap;
            }


        } else {
            retMap.put(Constants.ERROR_MESSAGE, jsonObject.getString("msg"));
            return retMap;
        }


        return retMap;
    }

    private String getRandomNumber(int count) {
        String[] arr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < count; i++) {
            int index = (int) Math.round(Math.random() * 9);
            String number = arr[index];
            sb.append(number);
        }

        return sb.toString();
    }

    /**
     * 上传用户头像
     *
     * @param request
     * @param files
     * @return
     */
    @RequestMapping("/uploadFace")
    @ResponseBody
    public Result uploadFile(HttpServletRequest request, @RequestParam("file") MultipartFile[] files) {
        Result result = new Result();
        result.setStatusCode(200);
        // 这样就可以收到文件了，files.length == 1.
        String id = request.getParameter("userId");
        if (files.length != 1) {
            result.setMessage("请检查上传图片数量");
            result.setSuccess(false);
        }
        MultipartFile file = files[0];
        //保存图片到磁盘
        String fileName = FileUtil.upload(file, path, file.getOriginalFilename());
        //更新用户信息表头像
        User user = new User();
        user.setId(id);
        user.setFaceImage(localPath + fileName);
        try {
            userService.updateUser(user);
            user = userService.queryUserById(id);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setStatusCode(502);
            result.setMessage("头像上传发生异常，请稍后重试");
            LOGGER.error("头像上传发生异常", e);
        }
        request.getSession().setAttribute(Constants.SESSION_USER, user);
        result.setUser(user);
        return result;
    }

    /**
     * 更新用户信息
     *
     * @param request
     * @param id
     * @param nickName
     * @return
     */
    @RequestMapping(value = "/modifyUserinfo")
    @ResponseBody
    public Result modifyUserinfo(HttpServletRequest request,
                                 @RequestParam(value = "id", required = true) String id,
                                 @RequestParam(value = "nickName") String nickName,
                                 @RequestParam(value = "email") String email,
                                 @RequestParam(value = "birthday") String birthday,
                                 @RequestParam(value = "sex") String sex) {
        Result result = new Result(true);
        result.setStatusCode(200);

        User user = userService.queryUserById(id);
        if (nickName != null && nickName != "") {
            user.setNickName(nickName);
        }
        if (email != null && email != "") {
            user.setEmail(email);
        }
        if (birthday != null && birthday != "") {
            user.setBirthday(birthday);
        }
        if (sex != null && sex != "") {
            user.setSex(sex);
        }
        try {
            userService.updateUser(user);
            user = userService.queryUserById(id);
            result.setUser(user);
        } catch (Exception e) {
            LOGGER.error("用户信息更新异常", e);
            result.setStatusCode(500);
            result.setMessage("用户信息更新异常");
        }
        request.getSession().setAttribute(Constants.SESSION_USER, user);
        return result;
    }

    @RequestMapping(value = "/UserSignOut")
    @ResponseBody
    public Result UserSignOut(HttpServletRequest request) throws IOException {
        Result result = new Result(true);
        result.setStatusCode(500);
        HttpSession session = request.getSession();
        request.getSession().removeAttribute(Constants.SESSION_USER);
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        if (user==null){
            result.setStatusCode(200);
        }
        return result;
    }
}
