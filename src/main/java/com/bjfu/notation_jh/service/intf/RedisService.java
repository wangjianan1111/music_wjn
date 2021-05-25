package com.bjfu.notation_jh.service.intf;

import com.bjfu.notation_jh.model.History;

import java.util.List;

/**
 * @Author john
 * @create 2020/9/6 17:05
 */
public interface RedisService {

    /**
     * 使用Redis将value存放到指定key中
     * @param key
     * @param value
     */
    void put(String key, String value);

    /**
     * 从redis中获取指定key的value值
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 获取redis的唯一数字
     * @return
     */
    Long getOnlyNumber();

    /**
     * 根据参数 count 的值，移除列表中与参数 value 相等的元素。
     * <p>
     * count 的值可以是以下几种：
     * <p>
     * count > 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count 。
     * count < 0 : 从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值。
     * count = 0 : 移除表中所有与 value 相等的值。
     * 返回值：
     * 被移除元素的数量。
     * 因为不存在的 key 被视作空表(empty list)，所以当 key 不存在时， LREM 命令总是返回 0 。
     */
    Long lrem(String key, Long count, Object value);

    /**
     * 实现命令 : LPUSH key 元素1 [元素2 ...]
     * 在最左端推入元素
     *
     * @param key
     * @param value
     * @return 执行 LPUSH命令后,列表的长度
     */
    long lpush(final String key, final Object value);

    /**
     * 实现命令 : LTRIM key 开始下标 结束下标
     * 裁剪 list。[01234] 的 `LTRIM key 1 -2` 的结果为 [123]
     *
     * @param key
     * @param start
     * @param end
     */
    void lTrim(String key, final Long start, Long end);

    /**
     * 设置key的生存时间
     * 单位:s
     *
     * @param key
     * @param seconds
     * @return
     */
    boolean expire(String key, Long seconds);

    /**
     * 实现命令 : LRANGE key 开始下标 结束下标
     * 获取指定范围的元素,下标从0开始，包括开始下标，也包括结束下标
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    List<Object> lrange(String key, final Long start, final Long end);

    /**
     * 实现命令 : LLEN key
     * 获取 list 的长度
     *
     * @param key
     * @return
     */
    Long llen(String key);

    /**
     * 删除一个或多个key
     *
     * @param keys
     * @return
     */
    Long del(String... keys);
}
