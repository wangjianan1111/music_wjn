package com.bjfu.notation_jh.controller;

import com.bjfu.notation_jh.common.response.Result;
import com.bjfu.notation_jh.enums.ResultEnum;
import com.bjfu.notation_jh.utils.FileUtil;
import com.bjfu.notation_jh.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/upload_notation")
public class UploadNotationController {

//    @Value("G:/000BJFU/imagesface")
    @Value("/profile/imagesface")
    private String path;
//    @Value("${web.image-path}")
//    private String imagePath;

    @PostMapping(value = "/addNotationPicture")
    @ResponseBody
    public Result addNotationPicture(@RequestParam(value = "file", required = false) MultipartFile file) {
        Result result = new Result();
        if (file == null) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "请选择要上传的图片");
        }
        if (file.getSize() > 1024 * 1024 * 10) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "文件大小不能大于10M");
        }
        //获取文件后缀
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1, file.getOriginalFilename().length());
        if (!"jpg,jpeg,png".toUpperCase().contains(suffix.toUpperCase())) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "请选择jpg,jpeg,png格式的图片");
        }
        //保存图片到磁盘
        if (file != null) {
            String fileName = FileUtil.upload(file, path, file.getOriginalFilename());
        }
        //1.调用自动识谱接口获取简谱内容，图片地址为filename
        //mock识谱接口回传数据(保存到notationchange表中)
        //TODO
        //2.转换简谱为前端键盘元素
        //TODO

        return result;
    }
}
