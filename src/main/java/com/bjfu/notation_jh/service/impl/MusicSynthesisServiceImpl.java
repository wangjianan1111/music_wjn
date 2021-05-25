package com.bjfu.notation_jh.service.impl;

import com.bjfu.notation_jh.dao.MusicSynthesisMapper;
import com.bjfu.notation_jh.model.MusicSynthesis;
import com.bjfu.notation_jh.service.intf.MusicSynthesisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author john
 * @create 2020/12/7 15:20
 */
@Service
public class MusicSynthesisServiceImpl implements MusicSynthesisService {

    @Autowired
    private MusicSynthesisMapper musicSynthesisMapper;

    @Override
    public int addMusicSynthesisRecord(MusicSynthesis musicSynthesis) {
        return musicSynthesisMapper.addMusicSynthesisRecord(musicSynthesis);
    }

    @Override
    public void updateMusicSynthesis(MusicSynthesis music) {
        musicSynthesisMapper.updateMusicSynthesis(music);
    }

    @Override
    public void updateMusicSynthesisByMuisicId(MusicSynthesis musicSynthesis) {
        musicSynthesisMapper.updateMusicSynthesisByMusicId(musicSynthesis);
    }

    @Override
    public List<MusicSynthesis> queryFinshedMusic(int i) {
        return musicSynthesisMapper.queryFinishedMusic(i);
    }
}
