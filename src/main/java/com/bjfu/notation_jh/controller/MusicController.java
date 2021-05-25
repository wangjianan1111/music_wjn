package com.bjfu.notation_jh.controller;

import com.alibaba.fastjson.JSONObject;
import com.bjfu.notation_jh.common.Constants;
import com.bjfu.notation_jh.common.response.Result;
import com.bjfu.notation_jh.model.BasicNotation;
import com.bjfu.notation_jh.model.MusicSynthesis;
import com.bjfu.notation_jh.model.NotationChange;
import com.bjfu.notation_jh.model.TmpNotation;
import com.bjfu.notation_jh.service.intf.*;
import com.bjfu.notation_jh.utils.*;
import javafx.geometry.Pos;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @Author john
 * @create 2020/12/6 23:36
 */
@Controller
@RequestMapping(value = "/music")
public class MusicController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MusicController.class);

//    @Value("G:/000BJFU/notationImages/")
    @Value("/profile/notationImages/")
    private String UPLOAD_FOLDER;

    @Value("/notationImages/")
    private String FOLDER;

    @Resource
    private BasicNotationService basicNotationService;

    @Resource
    private MusicSynthesisService musicSynthesisService;

    @Resource
    private NotationChangeService notationChangeService;

    @Resource
    private TmpNotationService tmpNotationService;

    @Resource
    private RedisService redisService;

    @Resource
    private MusicWorkService musicWorkService;

    /**
     * 歌声合成
     */
    @RequestMapping(value = "/synthesis", method = RequestMethod.POST)
    @ResponseBody
    public Result queryNotation(HttpServletRequest request, @RequestParam("file") MultipartFile[] files) {
        Result result = new Result(true);
        result.setStatusCode(200);
        String uid = request.getParameter("userId");
        String musicName = request.getParameter("musicName");
        int singer = Integer.parseInt(request.getParameter("singer"));
        String musicType = request.getParameter("musicType");

        int isPublic = Integer.parseInt(request.getParameter("isPublic"));
        String notationId = request.getParameter("notationId");
        if ("undefined".equals(notationId)) {
            notationId = "";
        }
        String tmpId = request.getParameter("tmpId");
        if ("undefined".equals(tmpId)) {
            tmpId = "";
        }
        String fileName = "";
        BasicNotation basicNotation = new BasicNotation();
        basicNotation.setNotationName(musicName);
        basicNotation.setNotationStyle(musicType);
        basicNotation.setIsPublic(isPublic);
        basicNotation.setUid(uid);
        basicNotation.setNotationDownloadUrl(fileName);
        basicNotation.setCreateTime(new Date());
        //从上传图片界面进入，且没有编辑过简谱
        if ((notationId == null || "".equals(notationId)) && (tmpId == null || "".equals(tmpId))) {
            if (files.length != 1) {
                result.setMessage("请检查上传图片数量");
                result.setSuccess(false);
                result.setStatusCode(500);
                return result;
            }
            MultipartFile file = files[0];
            //保存图片到磁盘
            String imgurl = FileUtil.upload(file, UPLOAD_FOLDER, file.getOriginalFilename());
            fileName = FOLDER + imgurl;
            basicNotation.setNotationDownloadUrl(imgurl);
            //1.保存简谱图片记录
            int count = basicNotationService.saveNotation(basicNotation);
            notationId = String.valueOf(basicNotation.getId());

            //从上传图片界面进入，编辑过简谱（tmpId不为空，notationid为空）
        } else if (tmpId != null && !"".equals(tmpId) && (notationId == null || "".equals(notationId))) {
            //根据tmpId查询简谱临时表
            TmpNotation tmpNotation = tmpNotationService.queryById(Long.valueOf(tmpId));
            basicNotation.setNotationDownloadUrl(tmpNotation.getImgUrl());
            int count = basicNotationService.saveNotation(basicNotation);
            fileName = tmpNotation.getImgUrl();

            //从search界面进入,编辑过简谱（tmpId不为空，notationid不为空）
        } else if (notationId != null && !"".equals(notationId) && tmpId != null && !"".equals(tmpId)) {
            TmpNotation tmpNotation = tmpNotationService.queryById(Long.valueOf(tmpId));
            basicNotation.setNotationDownloadUrl(tmpNotation.getImgUrl());
            int count = basicNotationService.saveNotation(basicNotation);
            fileName = tmpNotation.getImgUrl();

            //从search界面进入,未编辑过简谱（tmpId为空，notationid不为空）
        } else if (notationId != null && !"".equals(notationId)) {
            //根据notationid查询简谱表
            BasicNotation basicNotation1 = new BasicNotation();
            basicNotation1.setId(Long.valueOf(notationId));
            List<BasicNotation> list = basicNotationService.queryNotation(basicNotation1, 0, 1);
            fileName = list.get(0).getNotationDownloadUrl();
        }
        //更新前端参数表中的notationid,不需要

        MusicSynthesis musicSynthesis = new MusicSynthesis();
        musicSynthesis.setSinger(singer);
        musicSynthesis.setImageUrl(fileName);
        musicSynthesis.setMusicType(musicType);
        musicSynthesis.setIsPublic(isPublic);
        musicSynthesis.setMusicName(musicName);
        musicSynthesis.setUid(uid);
        musicSynthesis.setFinished(0);
        musicSynthesis.setCreateTime(new Date());
        if ((notationId != null && !"".equals(notationId))) {
            musicSynthesis.setNotationId(Long.valueOf(notationId));
        }
        ParamChangeUtils util = new ParamChangeUtils();
        Long key;
        try {
            // 2.新增歌声合成记录表，用户合成歌声新增一条记录
            int count = musicSynthesisService.addMusicSynthesisRecord(musicSynthesis);
            key = musicSynthesis.getId();
            //3.调用歌声合成接口（tmpId是否为空确定哪种情况，
            // 空：第一种情况，用户未编辑过简谱，先调用识别简谱接口，返回参数，参数转换后再调用歌声合成接口
            // 不空：第二种情况，用户编辑过简谱，根据userid和notationid唯一确定一条数据，
            // 拿到前端输入参数，转换为歌声合成接口参数）
            String paramUrl = "";
            HttpUtil httpUtil = new HttpUtil();
            if (tmpId == null || "".equals(tmpId)) {
                //第一种情况
                String realFileUrl = UPLOAD_FOLDER + fileName;
                //调用简谱识别接口
//                JSONObject jsonObject = httpUtil.doPost("recognize_notation", realFileUrl);
                //mock自动识谱接口的返回参数
                MockUtils mock = new MockUtils();
                String name = "";
                if ("阿果吉曲".equals(musicName)) {
                    name = "aguojiqu.jpg";
                } else if ("百花香".equals(musicName)) {
                    name = "baihuaxiang.jpg";
                } else if ("王昭君".equals(musicName)) {
                    name = "wangzhaojun.jpg";
                } else {
                    name = "aguojiqu.jpg";
                }
                JSONObject jsonObject = mock.getRecognizeParam(name);
                // 解析jsonObject，转换歌声合成参数，并返回
                paramUrl = util.changeRecognieParams2Synthesis(jsonObject);
            } else {
                //第二种情况
                //根据uid和tmpId拿到content（t_notation_change）
                NotationChange param = notationChangeService.queryChangeParam(uid, tmpId);
                //返回的是合成参数在本地临时的存储路径
                if (param == null) {
                    result.setMessage("系统异常");
                    result.setSuccess(false);
                    result.setStatusCode(500);
                    return result;
                }
                paramUrl = util.changeKeyboard2Synthesis(param);
            }
            //调用歌声合成接口
            Map<String, Object> map = new HashMap<>();
            if (singer==1){
                map = httpUtil.ocrDiscern(paramUrl, "music_synthesis_1");
            }else {
                map = httpUtil.ocrDiscern(paramUrl, "music_synthesis_2");
            }
//            PostUtil postUtil = new PostUtil();
//            String tmp = postUtil.officialFileUpload("music_synthesis", paramUrl);
            //mock歌声合成接口返回参数
//            MockUtils mock = new MockUtils();
//            Map<String, Object> map = mock.getMusic();
            //如果返回200，则更新记录表music_id
            int code = (int) map.get("code");
            if (code >= 20000 && code < 30000) {
                String musicId = (String) map.get("file_id");
                MusicSynthesis music = new MusicSynthesis();
                music.setId(key);
                music.setMusicId(musicId);
                musicSynthesisService.updateMusicSynthesis(music);
                //将musicId放到redis中
                if (singer==1){
                    redisService.lpush(Constants.MUSICLIST_1, musicId);
                }else {
                    redisService.lpush(Constants.MUSICLIST_2, musicId);
                }
            } else {
                String message = (String) map.get("message");
                LOGGER.error("远程接口调用异常，请稍后重试", message);
                result.setStatusCode(500);
                result.setMessage(message);
                result.setSuccess(false);
            }
        } catch (Exception e) {
            LOGGER.error("接口调用异常，请稍后重试", e);
            result.setStatusCode(500);
            result.setSuccess(false);
        }
        return result;
    }


    /**
     * 测试歌声合成接口
     */
    @RequestMapping(value = "/testMusic", method = RequestMethod.POST)
    @ResponseBody
    public void testMusic() {
        String realFileUrl = "G:\\000BJFU\\0cf595be-6893-41c8-871d-aea6ff553f63.txt";
        //            JSONObject jsonObject = httpUtil.doPost("music_synthesis", realFileUrl);
//        PostUtil postUtil = new PostUtil();
//        String tmp = postUtil.officialFileUpload("music_synthesis", realFileUrl);
        HttpUtil httpUtil = new HttpUtil();
        try {
            Map<String, Object> map = httpUtil.ocrDiscern(realFileUrl, "music_synthesis");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("调用完成");
    }

    /**
     * 删除作品
     */
    @RequestMapping(value = "/deletesb", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteMusic(HttpServletRequest request) {
        Result result = new Result(true);
        result.setStatusCode(200);
        String id = request.getParameter("id");
        if (id != null && !"".equals(id)) {
            try {
                musicWorkService.deleteMusic(Long.valueOf(id));
            } catch (Exception e) {
                LOGGER.error("删除失败", e);
                result.setStatusCode(500);
                result.setSuccess(false);
                throw e;
            }
        }else {
            result.setMessage("请检查id是否正确");
        }
        return result;
    }

}
