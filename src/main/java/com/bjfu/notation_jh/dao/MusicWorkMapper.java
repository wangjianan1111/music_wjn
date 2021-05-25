package com.bjfu.notation_jh.dao;

import com.bjfu.notation_jh.common.request.WorkSearchRequest;
import com.bjfu.notation_jh.model.MusicWork;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MusicWorkMapper {
    /**
     * 查询用户已有作品/收藏简谱
     * @param searchRequest
     * @return
     */
    List<MusicWork> queryWorksByPage(WorkSearchRequest searchRequest);

    /**
     * 保存用户收藏简谱
     * @param musicWork
     */
    void saveCollectionNotation(MusicWork musicWork);

    /**
     * 查询用户作品/收藏总数
     * @param searchRequest
     * @return
     */
    int queryMusicCount(@Param("sr") WorkSearchRequest searchRequest);

    /**
     * 根据id删除作品
     * @param id
     */
    void deleteMusic(Long id);
}
