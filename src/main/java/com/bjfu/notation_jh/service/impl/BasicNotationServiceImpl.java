package com.bjfu.notation_jh.service.impl;

import com.bjfu.notation_jh.dao.NotaionMapper;
import com.bjfu.notation_jh.model.BasicNotation;
import com.bjfu.notation_jh.service.intf.BasicNotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author john
 * @create 2020/11/26 16:28
 */
@Service
public class BasicNotationServiceImpl implements BasicNotationService {

    @Autowired
    private NotaionMapper notaionMapper;

    @Override
    public List<BasicNotation> queryNotation(BasicNotation bn, int pageNum, int pageSize) {
        return notaionMapper.queryNotation(bn, pageNum*pageSize, pageSize);
    }

    @Override
    public int queryNotationCount(BasicNotation bn, int pageSize) {
        int count = notaionMapper.queryNotationCount(bn);
        return count % pageSize == 0 ? (count / pageSize) : (count / pageSize + 1);
    }

    @Override
    public int saveNotation(BasicNotation basicNotation) {
        return notaionMapper.saveNotation(basicNotation);
    }

    @Override
    public void deleteNotation(Long id) {
        notaionMapper.deleteNotaiton(id);
    }
}
