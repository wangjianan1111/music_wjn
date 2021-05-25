package com.bjfu.notation_jh.service.intf;

import com.bjfu.notation_jh.model.MusicType;

import java.util.List;

/**
 * @Author john
 * @create 2020/12/6 11:03
 */
public interface MusicTypeService {
    /**
     * 查询所有歌曲类型
     * @return
     */
    List<MusicType> queryMusicType();
}
