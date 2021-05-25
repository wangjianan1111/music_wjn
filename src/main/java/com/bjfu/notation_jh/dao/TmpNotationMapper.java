package com.bjfu.notation_jh.dao;

import com.bjfu.notation_jh.model.TmpNotation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author john
 * @create 2020/12/10 10:51
 */
@Mapper
public interface TmpNotationMapper {

    /**
     * 根据id查询临时表
     * @param id
     * @return
     */
    TmpNotation queryById(@Param("id") Long id);

    /**
     * 保存临时表
     * @param tmpNotation
     * @return
     */
    int saveTmpNotation(TmpNotation tmpNotation);

    /**
     * 更新临时表
     * @param tmpNotation
     */
    void updateTmpNotation(@Param("tmpNotation") TmpNotation tmpNotation);
}
