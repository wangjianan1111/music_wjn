package com.bjfu.notation_jh.service.intf;

import com.bjfu.notation_jh.common.request.WorkSearchRequest;
import com.bjfu.notation_jh.model.BasicNotation;
import com.bjfu.notation_jh.model.MusicWork;

import java.util.List;

/**
 * @Author john
 * @create 2020/9/19 14:56
 */
public interface MusicWorkService {

    /**
     * 查询用户音乐作品
     * @param searchRequest
     * @return
     */
    List<MusicWork> queryWorksByPage(WorkSearchRequest searchRequest);

    /**
     * 查询作品/收藏总数
     * @param searchRequest
     * @return
     */
    int queryMusicCount(WorkSearchRequest searchRequest);

    /**
     * 保存用户收藏简谱
     * @param musicWork
     */
    void saveCollectNotation(MusicWork musicWork);

    /**
     * 根据id删除作品
     * @param id
     */
    void deleteMusic(Long id);
}
