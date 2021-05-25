package com.bjfu.notation_jh.service.intf;

import com.bjfu.notation_jh.common.request.WorkSearchRequest;
import com.bjfu.notation_jh.model.BasicNotation;
import com.bjfu.notation_jh.model.MusicWork;
import com.bjfu.notation_jh.model.Singer;

import java.util.List;

/**
 * @Author john
 * @create 2020/9/19 14:56
 */
public interface BasicNotationService {

    /**
     * 根据条件查询简谱
     *
     * @param bn
     * @return
     */
    public List<BasicNotation> queryNotation(BasicNotation bn, int pageNum, int pageSize);

    /**
     * 查询简谱总数
     * @param bn
     * @param pageSize
     * @return
     */
    public int queryNotationCount(BasicNotation bn, int pageSize);

    /**
     * 保存简谱
     * @param basicNotation
     */
    int saveNotation(BasicNotation basicNotation);

    /**
     * 根据id删除简谱
     * @param valueOf
     */
    void deleteNotation(Long valueOf);
}
