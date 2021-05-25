package com.bjfu.notation_jh.dao;

import com.bjfu.notation_jh.model.MusicSynthesis;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author john
 * @create 2020/12/7 16:26
 */
public interface MusicSynthesisMapper {

    /**
     * 增加用户合成记录
     * @param musicSynthesis
     */
    int addMusicSynthesisRecord(@Param("musicSynthesis") MusicSynthesis musicSynthesis);

    /**
     * 更新合成记录表
     * @param music
     */
    void updateMusicSynthesis(@Param("music") MusicSynthesis music);

    /**
     * 根据musicid更新合成记录表
     * @param musicSynthesis
     */
    void updateMusicSynthesisByMusicId(@Param("music") MusicSynthesis musicSynthesis);

    /**
     * 获取finished状态的记录数据
     * @param i
     * @return
     */
    List<MusicSynthesis> queryFinishedMusic(@Param("finished") int i);
}
