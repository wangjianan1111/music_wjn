package com.bjfu.notation_jh.utils;

import com.alibaba.fastjson.JSONObject;
import com.bjfu.notation_jh.common.Constants;
import com.bjfu.notation_jh.model.MusicSynthesis;
import com.bjfu.notation_jh.model.MusicWork;
import com.bjfu.notation_jh.service.intf.MusicSynthesisService;
import com.bjfu.notation_jh.service.intf.MusicWorkService;
import com.bjfu.notation_jh.service.intf.RedisService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author john
 * @create 2020/12/10 17:10
 * 定时任务：每s秒查询接口，返回歌声合成情况，若合成完成，增加用户作品数据到作品表中，
 * 并将该music_id从redis中删除
 */
@Component
public class MusicSchedule {

    private static final Logger LOGGER = LoggerFactory.getLogger(MusicSchedule.class);

    @Resource
    private RedisService redisService;

    @Resource
    private MusicSynthesisService musicSynthesisService;

    @Value("http://student.dengfeng.fun:60003/download?id=")
    private String downloadUrl_1;

    @Value("http://student.dengfeng.fun:60004/download?id=")
    private String downloadUrl_2;

//    @Value("G:/000BJFU/notationImages/")
    @Value("/profile/notationImages/")
    private String localFileUrl;

    @Value("/notationImages/")
    private String FOLDER;

    @Resource
    private MusicWorkService musicWorkService;

    /**
     * 该方法是调用check接口，获取当前歌声合成的状态并更新到合成记录表
     */
    //每分钟执行一次
    @Scheduled(cron = "0 */1 * * * ?")
    public void musicTask() {
        //执行第一个list
        LOGGER.info("================check music1 status===========");
        //1.从redis中获取需要查询的任务id列表
        List<Object> list1 = redisService.lrange(Constants.MUSICLIST_1, 0L, -1L);
        if (CollectionUtils.isEmpty(list1)) {
            LOGGER.info("================check music1 list is null===========");
            return;
        }
        LOGGER.info("================check music1 list size = " + list1.size() + "===========");
        List<String> musicIds = list1.stream().map(object -> {
            String musicId = (String) object;
            return musicId;
        }).collect(Collectors.toList());
        //多线程？？？（暂时用不上）
        //循环调用远程接口查询
        HttpUtil httpUtil = new HttpUtil();
        for (String musicId : musicIds) {
            LOGGER.info("================check music1 musicid is ====" + musicId + "===========");
            try {
//                JSONObject jsonObject = httpUtil.doPost("music_check", musicId);
                JSONObject jsonObject = httpUtil.doGet(musicId, "music_check_1");
                if (jsonObject == null) {
                    continue;
                }
                int code = (int) jsonObject.get("code");
                MusicSynthesis musicSynthesis = new MusicSynthesis();
                musicSynthesis.setMusicId(musicId);
                if (code == 20000) {
                    JSONObject jsonObject1 = (JSONObject) jsonObject.get("data");
                    String status = (String) jsonObject1.get("status");
                    LOGGER.info("=============" + musicId + "合成状态" + status + "==========");
                    if ("2".equals(status)) {
                        //合成完成
                        musicSynthesis.setFinished(1);
                        musicSynthesisService.updateMusicSynthesisByMuisicId(musicSynthesis);
                        redisService.lrem(Constants.MUSICLIST_1, 0L, musicId);
                    }
                } else if (code == 50002) {
                    LOGGER.info("=============" + musicId + "合成失败==========");
                    //合成失败
                    musicSynthesis.setFinished(2);
                    musicSynthesisService.updateMusicSynthesisByMuisicId(musicSynthesis);
                    redisService.lrem(Constants.MUSICLIST_1, 0L, musicId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        //执行第二个list
        LOGGER.info("================check music2 status===========");
        //1.从redis中获取需要查询的任务id列表
        List<Object> list2 = redisService.lrange(Constants.MUSICLIST_2, 0L, -1L);
        if (CollectionUtils.isEmpty(list2)) {
            LOGGER.info("================check music2 list is null===========");
            return;
        }
        LOGGER.info("================check music2 list size = " + list2.size() + "===========");
        List<String> musicIds_2 = list2.stream().map(object -> {
            String musicId = (String) object;
            return musicId;
        }).collect(Collectors.toList());
        //多线程？？？（暂时用不上）
        //循环调用远程接口查询
//        HttpUtil httpUtil = new HttpUtil();
        for (String musicId : musicIds_2) {
            LOGGER.info("================check music musicid is ====" + musicId + "===========");
            try {
//                JSONObject jsonObject = httpUtil.doPost("music_check", musicId);
                JSONObject jsonObject = httpUtil.doGet(musicId, "music_check_2");
                if (jsonObject == null) {
                    continue;
                }
                int code = (int) jsonObject.get("code");
                MusicSynthesis musicSynthesis = new MusicSynthesis();
                musicSynthesis.setMusicId(musicId);
                if (code == 20000) {
                    JSONObject jsonObject1 = (JSONObject) jsonObject.get("data");
                    String status = (String) jsonObject1.get("status");
                    LOGGER.info("=============" + musicId + "合成状态" + status + "==========");
                    if ("2".equals(status)) {
                        //合成完成
                        musicSynthesis.setFinished(1);
                        musicSynthesisService.updateMusicSynthesisByMuisicId(musicSynthesis);
                        redisService.lrem(Constants.MUSICLIST_2, 0L, musicId);
                    }
                } else if (code == 50002) {
                    LOGGER.info("=============" + musicId + "合成失败==========");
                    //合成失败
                    musicSynthesis.setFinished(2);
                    musicSynthesisService.updateMusicSynthesisByMuisicId(musicSynthesis);
                    redisService.lrem(Constants.MUSICLIST_2, 0L, musicId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    /**
     * 该方法是根据合成记录表中finished的状态值进行下一步操作：插入新的一条数据到作品表中/推送前端提示合成失败联系工作人员
     */
    //每分钟执行一次
    @Scheduled(cron = "0 */2 * * * ?")
    public void musicTask2() {

        //1.查询合成记录表-合成成功
        List<MusicSynthesis> list = musicSynthesisService.queryFinshedMusic(1);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        //2.循环每条记录，生成下载地址，插入作品表新的记录
        HttpUtil httpUtil = new HttpUtil();
        for (MusicSynthesis music : list) {
            //下载合成文件到本地
            if (music.getSinger()==1){
                httpUtil.downloadFile(downloadUrl_1 + music.getMusicId(), localFileUrl + music.getMusicId() + ".wav");
            }else{
                httpUtil.downloadFile(downloadUrl_2 + music.getMusicId(), localFileUrl + music.getMusicId() + ".wav");
            }
            MusicWork musicWork = new MusicWork();
            musicWork.setNotationId(music.getNotationId());
            musicWork.setRemark(0);
            musicWork.setNotationPic(music.getImageUrl());
            musicWork.setMusicName(music.getMusicName());
            musicWork.setUid(music.getUid());
            musicWork.setMusicDownloadUrl(FOLDER + music.getMusicId() + ".wav");
            musicWork.setSingerId(music.getSinger());
            musicWork.setMusicId(music.getMusicId());
            transactionTask(musicWork);
        }
    }

    //    @Scheduled(cron = "0 */1 * * * ?")
    public void musicTask3() {

        //1.查询合成记录表-合成失败
        List<MusicSynthesis> list = musicSynthesisService.queryFinshedMusic(2);
        for (MusicSynthesis music : list) {
            //推送给前端失败（或者不推送，在前端直接提示联系工作人员）
        }
    }

    @Transactional
    public void transactionTask(MusicWork musicWork) {
        try {
            musicWorkService.saveCollectNotation(musicWork);
            MusicSynthesis musicSynthesis = new MusicSynthesis();
            musicSynthesis.setFinished(3);
            musicSynthesis.setMusicId(musicWork.getMusicId());
            musicSynthesisService.updateMusicSynthesisByMuisicId(musicSynthesis);
        } catch (Exception e) {
            LOGGER.error("插入作品表和更新记录表失败", e);
        }

    }
}
