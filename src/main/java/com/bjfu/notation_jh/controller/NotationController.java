package com.bjfu.notation_jh.controller;

import com.bjfu.notation_jh.common.Constants;
import com.bjfu.notation_jh.common.request.NotationSearchRequest;
import com.bjfu.notation_jh.common.request.WorkSearchRequest;
import com.bjfu.notation_jh.common.response.Result;
import com.bjfu.notation_jh.model.*;
import com.bjfu.notation_jh.service.intf.*;
import com.bjfu.notation_jh.utils.FileUtil;
import com.bjfu.notation_jh.utils.ParamChangeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/notation")
public class NotationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotationController.class);

//    @Value("G:/000BJFU/notationImages")
    @Value("/profile/notationImages")
    private String UPLOAD_FOLDER;

    @Resource
    private NotationService notationService;

    @Resource
    private BasicNotationService basicNotationService;

    @Resource
    private MusicWorkService musicWorkService;

    @Resource
    private MusicTypeService musicTypeService;

    @Resource
    private TmpNotationService tmpNotationService;

    @Resource
    private NotationChangeService notationChangeService;

    /**
     * 获取所有歌手歌声
     */
    @RequestMapping(value = "/queryAllSinger", method = RequestMethod.GET)
    @ResponseBody
    public Result queryAllSinger(HttpServletRequest request) {
        Result result = new Result(true);
        List<Singer> singers = new ArrayList<>();
        try {
            singers = notationService.queryAllSinger();
        } catch (Exception e) {
            LOGGER.error("歌手信息查询", e);
            result.setSuccess(false);
        }
        result.setList(singers);
        return result;
    }

    /**
     * 获取所有歌曲分类
     */
    @RequestMapping(value = "/queryMusicType", method = RequestMethod.GET)
    @ResponseBody
    public Result queryMusicType(HttpServletRequest request){
        Result result = new Result(true);
        List<MusicType> musicTypes = new ArrayList<>();
//        try {
//            musicTypes = musicTypeService.queryMusicType();
//            throw new Exception("ceshi");
//        } catch (Exception e) {
//            LOGGER.error("歌曲分类查询", e);
//            result.setSuccess(false);
//        }
        musicTypes = musicTypeService.queryMusicType();
//        int i = 1/0;
        result.setList(musicTypes);
        return result;
    }

    /**
     * 根据简谱名称搜索简谱
     */
    @RequestMapping(value = "/queryNotation", method = RequestMethod.POST)
    @ResponseBody
    public Result queryNotation(HttpServletRequest request) {
        Result result = new Result(true);
        result.setStatusCode(200);
        List<BasicNotation> notations = new ArrayList<>();
        int totalPages = 0;
        try {
            NotationSearchRequest searchRequest = getSearchRequest(request);
            BasicNotation bn = new BasicNotation();
            BeanUtils.copyProperties(searchRequest, bn);
            totalPages = basicNotationService.queryNotationCount(bn, searchRequest.getPageSize());
            notations = basicNotationService.queryNotation(bn, searchRequest.getPageNum(), searchRequest.getPageSize());
        } catch (Exception e) {
            LOGGER.error("搜索简谱失败", e);
            result.setSuccess(false);
            result.setStatusCode(500);
        }
        result.setTotalPages(totalPages);
        result.setList(notations);
        return result;
    }

    /**
     * 根据id删除简谱
     */
    @RequestMapping(value = "/deleteNotation", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteNotation(HttpServletRequest request) {
        Result result = new Result(true);
        result.setStatusCode(200);
        String id = request.getParameter("id");
        if (id != null && !"".equals(id)) {
            try {
                basicNotationService.deleteNotation(Long.valueOf(id));
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

    /**
     * build请求参数
     *
     * @param request
     * @return
     */
    private NotationSearchRequest getSearchRequest(HttpServletRequest request) {
        NotationSearchRequest searchRequest = new NotationSearchRequest();
        searchRequest.setUid(request.getParameter("uid"));
        searchRequest.setNotationName(request.getParameter("notationName"));
        searchRequest.setNotationGroup(request.getParameter("notationGroup"));
        String style = request.getParameter("notationStyle");
        if (style != null && !"".equals(style) && !"0".equals(style)) {
            searchRequest.setNotationStyle(String.valueOf(Integer.parseInt(style)-1));
        }
        searchRequest.setNotationTag(request.getParameter("notationTag"));
        searchRequest.setPageNum(Integer.parseInt(request.getParameter("pageNum")));
        searchRequest.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
        String tmp = request.getParameter("isPublic");
        if (tmp != null && !"".equals(tmp)) {
            searchRequest.setIsPublic(Integer.parseInt(tmp));
        }
        return searchRequest;
    }

    /**
     * 查询用户所有作品
     */
    @RequestMapping(value = "/queryWorksByPage", method = RequestMethod.POST)
    @ResponseBody
    public Result queryWorksByPage(HttpServletRequest request) {
        Result result = new Result(true);
        result.setStatusCode(200);
        List<MusicWork> notations = new ArrayList<>();
        WorkSearchRequest searchRequest = new WorkSearchRequest();
        String notationId = request.getParameter("notationId");
        if (notationId != null && !"".equals(notationId)) {
            searchRequest.setNotationId(Long.valueOf(notationId));
        }
        searchRequest.setUid(request.getParameter("uid"));
        int pageNum = Integer.parseInt(request.getParameter("pageNum"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        searchRequest.setPageNum(pageNum * pageSize);
        searchRequest.setPageSize(pageSize);
        searchRequest.setRemark(Integer.parseInt(request.getParameter("remark")));
        String musicName = request.getParameter("musicName");
        if (musicName != null && !"".equals(musicName)) {
            searchRequest.setMusicName(musicName);
        }
        int totalPages = 0;
        try {
            notations = musicWorkService.queryWorksByPage(searchRequest);
            totalPages = musicWorkService.queryMusicCount(searchRequest);
        } catch (Exception e) {
            LOGGER.error("查询用户作品失败", e);
            result.setSuccess(false);
            result.setStatusCode(500);
        }
        result.setTotalPages(totalPages);
        result.setList(notations);
        return result;
    }

    /**
     * 查询用户收藏的简谱
     */
    @RequestMapping(value = "/queryCollectionNotation", method = RequestMethod.POST)
    @ResponseBody
    public Result queryCollectionNotation(HttpServletRequest request, @RequestParam(value = "searchRequest") WorkSearchRequest searchRequest) {
        Result result = new Result(true);
        List<MusicWork> notations = new ArrayList<>();
        try {
            //调用时remark必须（0-作品 1-收藏）
            notations = notationService.queryWorksByPage(searchRequest);
        } catch (Exception e) {
            LOGGER.error("查询用户收藏简谱失败", e);
            result.setSuccess(false);
        }
        result.setList(notations);
        return result;
    }

    /**
     * 保存用户收藏的简谱
     */
    @RequestMapping(value = "/saveCollectionNotation", method = RequestMethod.POST)
    @ResponseBody
    public Result saveCollectionNotation(HttpServletRequest request) {
        Result result = new Result(true);
        result.setStatusCode(200);
        String uid = request.getParameter("uid");
        String notationPic = request.getParameter("notationPic");
        String remark = request.getParameter("remark");
        String notationId = request.getParameter("notationId");
        String musicName = request.getParameter("musicName");
        MusicWork musicWork = new MusicWork();
        musicWork.setUid(uid);
        musicWork.setMusicName(musicName);
        musicWork.setNotationPic(notationPic);
        musicWork.setRemark(Integer.parseInt(remark));
        musicWork.setNotationId(Long.valueOf(notationId));
//        List<MusicWork> notations = new ArrayList<>();
        try {
            musicWorkService.saveCollectNotation(musicWork);
        } catch (Exception e) {
            LOGGER.error("用户保存简谱失败", e);
            result.setSuccess(false);
            result.setStatusCode(500);
        }
//        result.setList(notations);
        return result;
    }

    /**
     * 用户保存简谱
     * 用户使用简谱编辑器编辑的简谱
     * 1.生成图片保存到磁盘，并将url和基本信息保存到basicnotaion表中
     * 1.保存简谱到notationchange表
     */
    @RequestMapping(value = "/saveNotation", method = RequestMethod.POST)
    @ResponseBody
    public Result saveNotation(HttpServletRequest request, @RequestParam("file") MultipartFile[] files) {
        Result result = new Result(true);
        result.setStatusCode(200);
        String uid = request.getParameter("uid");
        String content = request.getParameter("content");
        String imgUrl = "";
        String notationId = request.getParameter("notationId");
        try {
            //1.保存简谱到简谱临时表中
            //先保存图片到磁盘？？？ 如果notationId为空，则需要保存图片，如果不为空则根据notationId查询地址
            if (notationId == null || "".equals(notationId)) {
                //上传图片
                if (files.length != 1) {
                    result.setMessage("请检查上传图片数量");
                    result.setSuccess(false);
                    result.setStatusCode(500);
                    return result;
                }
                MultipartFile file = files[0];
                //保存图片到磁盘
                imgUrl = FileUtil.upload(file, UPLOAD_FOLDER, file.getOriginalFilename());
            } else {
                //查询地址
                BasicNotation basicNotation = new BasicNotation();
                basicNotation.setId(Long.valueOf(notationId));
                List<BasicNotation> list = basicNotationService.queryNotation(basicNotation, 0, 1);
                imgUrl = list.get(0).getNotationDownloadUrl();
            }
            TmpNotation tmpNotation = new TmpNotation();
            tmpNotation.setUid(uid);
            tmpNotation.setImgUrl(imgUrl);
            int tmpId = tmpNotationService.saveTmpNotation(tmpNotation);
            result.setStr(String.valueOf(tmpId));
            //2.保存前端参数到notationchange表中
            NotationChange notationChange = new NotationChange();
            notationChange.setContent(content);
            notationChange.setCreateTime(new Date());
            notationChange.setNotationId(Long.valueOf(notationId));
            notationChange.setUid(uid);
            notationChange.setTmpId(tmpNotation.getId());
            notationChangeService.saveChangeParam(notationChange);
        } catch (Exception e) {
            result.setStatusCode(500);
            result.setSuccess(false);
            LOGGER.error("系统异常", e);
        }
        //增加返回tmpId
        return result;
    }


    @RequestMapping(value = "/updateNotation", method = RequestMethod.POST)
    @ResponseBody
    public Result updateNotation(HttpServletRequest request) {
        Result result = new Result(true);
        result.setStatusCode(200);

        String content = request.getParameter("content");
        String title = request.getParameter("title");
        String subTitle = request.getParameter("subTitle");
        String keyAndMeters = request.getParameter("keyAndMeters");
        String wordsByAndMusicBy = request.getParameter("wordsByAndMusicBy");
        String uid = request.getParameter("uid");
        String tmpId = request.getParameter("tmpId");
        ParamChangeUtils utils = new ParamChangeUtils();
        NotationChange notationChange = new NotationChange();
        User user = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        if (tmpId != null && !"".equals(tmpId)) {
            notationChange.setTmpId(Long.valueOf(tmpId));
        }
        notationChange.setUid(uid);
        notationChange.setContent(content);
        notationChange.setTitle(title);
        notationChange.setSubTitle(subTitle);
        notationChange.setKeyAndMeters(keyAndMeters);
        if (wordsByAndMusicBy==null||"".equals(wordsByAndMusicBy)){
            wordsByAndMusicBy = user.getNickName();
        }
        notationChange.setWordsAndMusicBy(wordsByAndMusicBy);
        String imgUrl = "";
        try {
            imgUrl = utils.changeKeyboard2Img(notationChange);
        } catch (Exception e) {
            LOGGER.error("生成图片异常", e);
            result.setStatusCode(500);
            result.setSuccess(false);
        }

        TmpNotation tmpNotation = new TmpNotation();
        tmpNotation.setUid(uid);
        if (tmpId != null && !"".equals(tmpId)) {
            tmpNotation.setId(Long.valueOf(tmpId));
        }
        tmpNotation.setImgUrl(imgUrl);
        try {
            //将数据保存到change表、临时表
            if (tmpId != null && !"".equals(tmpId)) {
                //更新
                notationChangeService.updateChangeParam(notationChange);
                tmpNotationService.updateTmpNotation(tmpNotation);
            } else {
                //新增保存
                int count = tmpNotationService.saveTmpNotation(tmpNotation);
                Long id = tmpNotation.getId();
                notationChange.setTmpId(id);
                notationChangeService.saveChangeParam(notationChange);
            }
        } catch (Exception e) {
            LOGGER.error("前端参数保存数据库失败", e);
            result.setStatusCode(500);
            result.setSuccess(false);
        }
        result.setTmpNotation(tmpNotation);
        return result;
    }
}
