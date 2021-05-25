package com.bjfu.notation_jh.service.intf;

import com.bjfu.notation_jh.model.MusicSynthesis;

import java.util.List;

/**
 * @Author john
 * @create 2020/12/7 15:20
 */
public interface MusicSynthesisService {
    /**
     * 增加用户歌声合成记录
     * @param musicSynthesis
     */
    int addMusicSynthesisRecord(MusicSynthesis musicSynthesis);

    /**
     * 更新用户合成记录表
     * @param music
     */
    void updateMusicSynthesis(MusicSynthesis music);

    /**
     * 更新用户合成记录表根据musicid
     * @param musicSynthesis
     */
    void updateMusicSynthesisByMuisicId(MusicSynthesis musicSynthesis);

    /**
     * 获取finished状态的记录数据
     * @param i
     * @return
     */
    List<MusicSynthesis> queryFinshedMusic(int i);
}
