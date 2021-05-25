package com.bjfu.notation_jh.service.impl;

import com.bjfu.notation_jh.common.request.WorkSearchRequest;
import com.bjfu.notation_jh.dao.MusicWorkMapper;
import com.bjfu.notation_jh.model.MusicWork;
import com.bjfu.notation_jh.service.intf.MusicWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author john
 * @create 2020/12/2 14:24
 */
@Service
public class MusicWorkServiceImpl implements MusicWorkService {

    @Autowired
    private MusicWorkMapper musicWorkMapper;

    @Override
    public List<MusicWork> queryWorksByPage(WorkSearchRequest searchRequest) {
        return musicWorkMapper.queryWorksByPage(searchRequest);
    }

    @Override
    public int queryMusicCount(WorkSearchRequest searchRequest) {
        int count = musicWorkMapper.queryMusicCount(searchRequest);
        int pageSize = searchRequest.getPageSize();
        return count % pageSize == 0 ? (count / pageSize) : (count / pageSize + 1);
    }

    @Override
    public void saveCollectNotation(MusicWork musicWork) {
        musicWorkMapper.saveCollectionNotation(musicWork);
    }

    @Override
    public void deleteMusic(Long id) {
        musicWorkMapper.deleteMusic(id);
    }
}
