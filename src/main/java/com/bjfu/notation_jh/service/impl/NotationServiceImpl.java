package com.bjfu.notation_jh.service.impl;

import com.bjfu.notation_jh.common.request.WorkSearchRequest;
import com.bjfu.notation_jh.dao.MusicWorkMapper;
import com.bjfu.notation_jh.dao.NotaionMapper;
import com.bjfu.notation_jh.dao.SingerMapper;
import com.bjfu.notation_jh.model.BasicNotation;
import com.bjfu.notation_jh.model.MusicWork;
import com.bjfu.notation_jh.model.Singer;
import com.bjfu.notation_jh.service.intf.NotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.collections.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotationServiceImpl implements NotationService {

    @Autowired
    private SingerMapper singerMapper;

//    @Autowired
//    private NotaionMapper notaionMapper;
//
//    @Autowired
//    private MusicWorkMapper musicWorkMapper;

    @Override
    public List<Singer> queryAllSinger() {
        List<Singer> singers = new ArrayList<>();
        try {
            singers = singerMapper.queryAllSinger();
        }catch (Exception e){
            System.out.println(e);
        }
        return singers;
    }

    @Override
    public List<BasicNotation> queryNotation(BasicNotation bn, int pageNum, int pageSize) {
//        return notaionMapper.queryNotation(bn, pageNum, pageSize);
        return null;
    }

    @Override
    public List<MusicWork> queryWorksByPage(WorkSearchRequest searchRequest) {
//        return musicWorkMapper.queryWorksByPage(searchRequest);
        return null;
    }

    @Override
    public void saveCollectionNotation(WorkSearchRequest searchRequest) {
        //1.查询简谱信息
        BasicNotation basicNotation = new BasicNotation();
        basicNotation.setId(searchRequest.getNotationId());
//        List<BasicNotation> basicNotationList = notaionMapper.queryNotation(basicNotation, 0, 1);
        List<BasicNotation> basicNotationList = null;
        MusicWork musicWork = new MusicWork();
        //构造workmusic
        if (CollectionUtils.isNotEmpty(basicNotationList)){
            musicWork.setRemark(1);
            musicWork.setNotationId(searchRequest.getNotationId());
            musicWork.setNotationPic(basicNotationList.get(0).getNotationPic());
//            musicWork.setUid(searchRequest.getUid());
            //保存收藏简谱
//            musicWorkMapper.saveCollectionNotation(musicWork);
        }
    }
}
