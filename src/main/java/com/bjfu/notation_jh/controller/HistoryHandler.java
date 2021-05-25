package com.bjfu.notation_jh.controller;


import com.bjfu.notation_jh.common.response.Result;
import com.bjfu.notation_jh.enums.SuffixEnum;
import com.bjfu.notation_jh.model.History;
import com.bjfu.notation_jh.service.intf.RedisService;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author jiahe
 */
@RestController
@RequestMapping("/browsingHistory")
public class HistoryHandler {

    @Autowired
    private RedisService redisService;

    /**
     * 默认过期时长，单位：秒
     */
    private final static long DEFAULT_EXPIRE = 60 * 60 * 24 * 30;

    public static final ObjectMapper mapper = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(HistoryHandler.class);

    /**
     * 存一个浏览简谱信息
     *
     * @param request
     */
    @PostMapping("/set")
    public void set(HttpServletRequest request) {
//        public void set(@Validated @RequestBody History history) {
//        String key = getKey(history.getCode(), history.getUid());
        History history = new History();
        String uid = request.getParameter("uid");
        String notationId = request.getParameter("notationId");
        String notationPic = request.getParameter("notationPic");
        history.setUid(uid);
        history.setNotationId(Long.valueOf(notationId));
        history.setNotationPic(notationPic);
        String key = history.getUid();
        /* 为了保证浏览商品的 唯一性,每次添加前,将list中该商品ID去掉,再加入,以保证其浏览的最新的商品在最前面 */
        redisService.lrem(key, 1L, history);
        /* 将value push 到该key下的list中 */
        redisService.lpush(key, history);
        /* 浏览记录存5条,五条以后切掉*/
        redisService.lTrim(key, 0L, 10L);
        /* 缓存时间为一个月 */
        redisService.expire(key, DEFAULT_EXPIRE);
    }

    /**
     * 获取当前用户的浏览历史
     *
     * @param request
     * @return
     */
    @PostMapping("/getByUid")
    public Result query(HttpServletRequest request) {
        Result result = new Result(true);
        result.setStatusCode(200);
//        String key = getKey(code, uid);
        String key = request.getParameter("uid");
        //这里可支持分页 start and end
        List<History> list = new ArrayList<>();
        try{
            List<Object> range = redisService.lrange(key, 0L, 9999L);
            list = range.stream().map(object->{
                History history = (History) object;
                return history;
            }).collect(Collectors.toList());
        }catch (Exception e){
            LOGGER.error("获取历史浏览记录失败",e);
            result.setStatusCode(500);
            result.setSuccess(false);
        }
        result.setList(list);
        return result;


        /* 以下代码解决json反序列化之后集合不能操作问题(例如使用Stream)
        String key = getKey(code, userId);
        List<HistoryPo> lrange = redisService.lrange(key, 0L, 9999L);
        JavaType javaType = getCollectionType(LinkedList.class, HistoryPo.class);
        List<HistoryPo> lst = null;
        try {
            lst = mapper.readValue(lrange.toString(), javaType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

    /**
     * 删除单个商品浏览历史
     *
     * @param history
     * @return
     */
    @DeleteMapping("/del")
    public Long del(@Validated @RequestBody History history) {
//        String key = getKey(history.getCode(), history.getUid());
        String key = history.getUid();
        return redisService.lrem(key, 0L, history);
    }

    /**
     * 删除所有商品浏览历史
     *
     * @param code
     * @param userId
     * @return
     */
    @DeleteMapping(value = "/delAll/{code}/{userId}")
    public Long delAll(@PathVariable("code") Integer code, @PathVariable("userId") Long userId) {
        String key = getKey(code, userId);
        return redisService.del(key);
    }

    /**
     * 获取当前用户的key,
     *
     * @param code   区分pc和平台端
     * @param userId 用户id
     * @return userId + pc or mobile
     */
    private String getKey(Integer code, Long userId) {
        String key = userId.toString();
        return StringUtils.join(key, SuffixEnum.getSuffix(code));
    }

    /**
     * 获取泛型的Collection Type
     *
     * @param collectionClass 泛型的Collection
     * @param elementClasses  元素类
     * @return JavaType Java类型
     * @since 1.0
     */
    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }
}
