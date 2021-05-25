package com.bjfu.notation_jh.service.intf;

import com.bjfu.notation_jh.model.TmpNotation;

/**
 * @Author john
 * @create 2020/12/10 10:10
 */
public interface TmpNotationService {

    /**
     * 根据id查询前端参数表
     * @param id
     * @return
     */
    TmpNotation queryById(Long id);

    /**
     * 保存前端参数
     * @param tmpNotation
     * @return
     */
    int saveTmpNotation(TmpNotation tmpNotation);

    /**
     * 更新临时表
     * @param tmpNotation
     */
    void updateTmpNotation(TmpNotation tmpNotation);
}
