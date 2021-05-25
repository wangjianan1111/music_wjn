package com.bjfu.notation_jh.controller;

import com.alibaba.fastjson.JSONObject;
import com.bjfu.notation_jh.common.response.Result;
import com.bjfu.notation_jh.model.BasicNotation;
import com.bjfu.notation_jh.model.NotationChange;
import com.bjfu.notation_jh.service.intf.BasicNotationService;
import com.bjfu.notation_jh.utils.FileUtil;
import com.bjfu.notation_jh.utils.HttpUtil;
import com.bjfu.notation_jh.utils.MockUtils;
import com.bjfu.notation_jh.utils.ParamChangeUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @Author john
 * @create 2020/9/6 18:40
 */

/**
 * 1.接收简谱图片保存并调用识谱接口
 * 2.接收录音调用识谱接口
 */
@Controller
@RequestMapping(value = "/upload")
public class RecognitionNotationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecognitionNotationController.class);

//    @Value("G:/000BJFU/notationImages/")
    @Value("/profile/notationImages/")
    private String UPLOAD_FOLDER;

//    @Value("G:/000BJFU")
    @Value("/profile")
    private String ROOT_FOLDER;

    @Autowired
    private BasicNotationService basicNotationService;

//    @PostMapping("/upload")
//    public Result upload(@RequestParam(name = "file", required = false) MultipartFile file, HttpServletRequest request) {
//        if (file == null) {
//            return ResultUtil.error(ResultEnum.FAIL.getCode(), "请选择要上传的图片");
//        }
//        if (file.getSize() > 1024 * 1024 * 10) {
//            return ResultUtil.error(ResultEnum.FAIL.getCode(), "文件大小不能大于10M");
//        }
//        //获取文件后缀
//        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1, file.getOriginalFilename().length());
//        if (!"jpg,jpeg,png".toUpperCase().contains(suffix.toUpperCase())) {
//            return ResultUtil.error(ResultEnum.FAIL.getCode(), "请选择jpg,jpeg,png格式的图片");
//        }
//        String savePath = UPLOAD_FOLDER;
//        File savePathFile = new File(savePath);
//        if (!savePathFile.exists()) {
//            //若不存在该目录，则创建目录
//            savePathFile.mkdir();
//        }
//        //通过UUID生成唯一文件名
//        String filename = UUID.randomUUID().toString().replaceAll("-", "") + "." + suffix;
//        try {
//            //将文件保存指定目录
//            file.transferTo(new File(savePath + filename));
//            //将文件路径保存到db
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResultUtil.error(ResultEnum.FAIL.getCode(), "保存文件异常");
//        }
//        //返回文件名称
//        return ResultUtil.success(ResultEnum.SUCCESS.getCode(), filename);
//    }

    /**
     * 简谱识别（上传图片到临时文件夹中-》调用自动识谱接口返回参数-》根据参数生成简谱图片-》返回前端简谱图片+键盘参数）
     *
     * @param request
     * @param files
     * @return
     */
    @RequestMapping("/recognizenotation")
    @ResponseBody
    public Result uploadFile(HttpServletRequest request, @RequestParam("file") MultipartFile[] files) throws IOException {
        Result result = new Result(true);
        result.setStatusCode(200);

        // 1.上传图片到临时文件夹中
        String id = request.getParameter("userId");
        if (files.length != 1) {
            result.setMessage("请检查上传图片数量");
            result.setSuccess(false);
            result.setStatusCode(500);
            return result;
        }
        String notationId = request.getParameter("notationId");
        String fileName = "";
        String name = "";
        if (notationId == null || "".equals(notationId) || "undefined".equals(notationId)) {
            MultipartFile file = files[0];
            //保存图片到磁盘
            fileName =UPLOAD_FOLDER +  FileUtil.upload(file, UPLOAD_FOLDER, file.getOriginalFilename());
            name = file.getOriginalFilename();
        } else {
            //简谱表中查询地址
            BasicNotation basicNotation = new BasicNotation();
            basicNotation.setId(Long.valueOf(notationId));
            List<BasicNotation> list = basicNotationService.queryNotation(basicNotation, 0, 1);
            if (CollectionUtils.isNotEmpty(list)) {
                name = list.get(0).getNotationPic();
                fileName = ROOT_FOLDER + list.get(0).getNotationDownloadUrl();
            }
        }

        // 2.调用自动识谱接口返回参数
        //接口入参为图片地址
//        String realFileUrl = UPLOAD_FOLDER + fileName;
        HttpUtil httpUtil = new HttpUtil();
//        JSONObject jsonObject = httpUtil.doPost("recognize_notation", fileName);
        //mock自动识谱接口的返回参数
        MockUtils mock = new MockUtils();
        JSONObject jsonObject = mock.getRecognizeParam(name);
        // 3.解析jsonObject，转换前端参数，并返回
        ParamChangeUtils util = new ParamChangeUtils();
        NotationChange notationChange = util.changeRecognieParams2Keyboard(jsonObject);
        result.setObject(notationChange);
        return result;
    }

}
