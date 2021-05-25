package com.bjfu.notation_jh.service.impl;

import com.bjfu.notation_jh.dao.NotationChangeMapper;
import com.bjfu.notation_jh.model.NotationChange;
import com.bjfu.notation_jh.service.intf.NotationChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author john
 * @create 2020/12/9 16:26
 */
@Service
public class NotationChangeServiceImpl implements NotationChangeService {

    @Autowired
    private NotationChangeMapper notationChangeMapper;

    @Override
    public NotationChange queryChangeParam(String uid, String tmpId) {
        return notationChangeMapper.queryChangeParam(uid,tmpId);
    }

    @Override
    public void saveChangeParam(NotationChange notationChange) {
        notationChangeMapper.saveChangeParam(notationChange);
    }

    @Override
    public void updateChangeParam(NotationChange notationChange) {
        notationChangeMapper.updateChangeParam(notationChange);
    }
}
