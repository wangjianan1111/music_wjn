package com.bjfu.notation_jh.service.impl;

import com.bjfu.notation_jh.dao.MusicTypeMapper;
import com.bjfu.notation_jh.model.MusicType;
import com.bjfu.notation_jh.service.intf.MusicTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author john
 * @create 2020/12/6 11:04
 */
@Service
public class MusicTypeServiceImpl implements MusicTypeService {

    @Autowired
    private MusicTypeMapper musicTypeMapper;

    @Override
    public List<MusicType> queryMusicType() {
        return musicTypeMapper.queryMusicType();
    }
}
