package com.bjfu.notation_jh.service.impl;

import com.bjfu.notation_jh.dao.TmpNotationMapper;
import com.bjfu.notation_jh.model.TmpNotation;
import com.bjfu.notation_jh.service.intf.TmpNotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author john
 * @create 2020/12/10 10:10
 */
@Service
public class TmpNotationServiceImpl implements TmpNotationService {

    @Autowired
    private TmpNotationMapper tmpNotationMapper;

    @Override
    public TmpNotation queryById(Long id) {
        return tmpNotationMapper.queryById(id);
    }

    @Override
    public int saveTmpNotation(TmpNotation tmpNotation) {
        return tmpNotationMapper.saveTmpNotation(tmpNotation);
    }

    @Override
    public void updateTmpNotation(TmpNotation tmpNotation) {
        tmpNotationMapper.updateTmpNotation(tmpNotation);
    }
}
