package com.bjfu.notation_jh.service.intf;

import com.bjfu.notation_jh.common.request.WorkSearchRequest;
import com.bjfu.notation_jh.model.BasicNotation;
import com.bjfu.notation_jh.model.MusicWork;
import com.bjfu.notation_jh.model.Singer;
import org.springframework.cache.annotation.Cacheable;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author john
 * @create 2020/9/19 14:56
 */
public interface NotationService {
    /**
     * 查询所有歌手
     * @return
     */
//    @Cacheable
    List<Singer> queryAllSinger();

    /**
     * 根据条件查询简谱
     * @param bn
     * @return
     */
    List<BasicNotation> queryNotation(BasicNotation bn, int pageNum, int pageSize);

    /**
     * 分页查询用户作品
     * @param searchRequest
     * @return
     */
    List<MusicWork> queryWorksByPage(WorkSearchRequest searchRequest);

    /**
     * 保存用户收藏简谱
     * @param searchRequest
     */
    void saveCollectionNotation(WorkSearchRequest searchRequest);
}
