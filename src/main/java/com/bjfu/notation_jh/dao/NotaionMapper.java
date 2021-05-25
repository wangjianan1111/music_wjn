package com.bjfu.notation_jh.dao;

import com.bjfu.notation_jh.common.request.WorkSearchRequest;
import com.bjfu.notation_jh.model.BasicNotation;
import com.bjfu.notation_jh.model.MusicWork;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotaionMapper {

    /**
     * 根据条件查询简谱
     * @param bn
     * @return
     */
    public List<BasicNotation> queryNotation(@Param("bn") BasicNotation bn, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

    /**
     * 分页查询用户作品
     * @param searchRequest
     * @return
     */
    public List<MusicWork> queryWorksByPage(WorkSearchRequest searchRequest);

    /**
     * 根据条件查询简谱总数
     * @param bn
     * @return
     */
    public int queryNotationCount(@Param("bn") BasicNotation bn);

    /**
     * 保存简谱
     * @param basicNotation
     */
    int saveNotation(BasicNotation basicNotation);

    /**
     * 根据id删除简谱
     * @param id
     */
    void deleteNotaiton(Long id);
}
