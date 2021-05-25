package com.bjfu.notation_jh.dao;

import com.bjfu.notation_jh.model.MusicType;
import com.bjfu.notation_jh.model.Singer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MusicTypeMapper {

    /**
     * 查询所有歌曲分类
     * @return
     */
    List<MusicType> queryMusicType();
}
