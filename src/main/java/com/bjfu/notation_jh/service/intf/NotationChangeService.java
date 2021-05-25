package com.bjfu.notation_jh.service.intf;

import com.bjfu.notation_jh.model.NotationChange;

/**
 * @Author john
 * @create 2020/12/9 16:26
 */
public interface NotationChangeService {
    /**
     * 获取转换参数
     * @param uid
     * @param notationId
     * @return
     */
    NotationChange queryChangeParam(String uid, String tmpId);

    /**
     * 保存前端参数
     * @param notationChange
     */
    void saveChangeParam(NotationChange notationChange);

    /**
     * 更新前端参数表
     * @param notationChange
     */
    void updateChangeParam(NotationChange notationChange);
}
