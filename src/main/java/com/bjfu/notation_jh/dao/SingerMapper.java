package com.bjfu.notation_jh.dao;

import com.bjfu.notation_jh.model.Singer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SingerMapper {
    /**
     * 查询所有歌手信息
     * @return
     */
    List<Singer> queryAllSinger();
}
