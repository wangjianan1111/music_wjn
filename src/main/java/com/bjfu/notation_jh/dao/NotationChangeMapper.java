package com.bjfu.notation_jh.dao;

import com.bjfu.notation_jh.model.NotationChange;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author john
 * @create 2020/12/9 17:00
 */
@Mapper
public interface NotationChangeMapper {

    /**
     * 获取转换参数
     * @param uid
     * @param tmpId
     * @return
     */
    NotationChange queryChangeParam(@Param("uid") String uid, @Param("tmpId") String tmpId);

    /**
     * 保存前端参数
     * @param notationChange
     */
    void saveChangeParam(@Param("notationChange") NotationChange notationChange);

    /**
     * 更新前端参数表
     * @param notationChange
     */
    void updateChangeParam(@Param("notationChange") NotationChange notationChange);
}
