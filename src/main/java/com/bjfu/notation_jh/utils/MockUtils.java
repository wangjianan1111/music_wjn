package com.bjfu.notation_jh.utils;

import com.alibaba.fastjson.JSONObject;
import com.bjfu.notation_jh.common.response.RecognizeVO;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author john
 * @create 2020/12/20 16:55
 */
public class MockUtils {

    /**
     * mock自动识谱接口返回参数
     *
     * @return
     */
    public JSONObject getRecognizeParam(String name) throws IOException {
        RecognizeVO vo = new RecognizeVO();
        String path = "";
        if ("aguojiqu.jpg".equals(name)) {
            path = "static/assets/aguojiqu.txt";
        } else if ("baihuaxiang.jpg".equals(name)) {
            path = "static/assets/baihuaxiang.txt";
        } else if ("wangzhaojun.jpg".equals(name)) {
            path = "static/assets/wangzhaojun.txt";
        }

        ClassPathResource classPathResource = new ClassPathResource(path);
        InputStream inputStream = null;
        StringBuilder buffer = new StringBuilder();
        try {
            inputStream = classPathResource.getInputStream();

            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream));

            while (bufferedReader.ready()) {
                buffer.append((char) bufferedReader.read());
            }

            String a = buffer.toString();

            bufferedReader.close();
            bufferedInputStream.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        String str = buffer.toString();
        String[] strs = str.split(";");
        String[] starts = strs[0].substring(1, strs[0].length() - 1).split(",");
        List<String> st = new ArrayList();
        for (int i = 0; i < starts.length; i++) {
            st.add(starts[i].trim());
        }
        vo.setStart(st);
        String[] names = strs[1].substring(1, strs[1].length() - 1).split(",");
        List<String> na = new ArrayList();
        for (int i = 0; i < names.length; i++) {
            na.add(names[i].trim());
        }
        vo.setName(na);
//        System.out.println(strs[strs.length - 1]);
//        System.out.println((strs[strs.length - 1].substring(1, strs[strs.length - 1].length() - 1)));
//        System.out.println((strs[strs.length - 1].substring(1, strs[strs.length - 1].length() - 1)).split(","));
        String[] lycirss = (strs[strs.length - 1].substring(1, strs[strs.length - 1].length() - 1)).split(",",-1);
        List<String> ly = new ArrayList();
        for (int i = 0; i < lycirss.length; i++) {
            ly.add(lycirss[i].trim());
        }
        vo.setLycirs(ly);
        List<List<String>> note = new ArrayList<>();
        for (int i = 2; i < strs.length - 1; i++) {
            String[] tmp = strs[i].substring(1, strs[i].length() - 1).split(",");
            List<String> subNote = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                if (j > tmp.length - 1) {
                    subNote.add("");
                } else {
                    subNote.add(tmp[j].trim());
                }
            }
            note.add(subNote);
        }
        vo.setNote(note);
        return (JSONObject) JSONObject.toJSON(vo);
    }

    public static void main(String[] args) {
        MockUtils mock = new MockUtils();
        try {
            mock.getRecognizeParam("阿果吉曲");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 歌声合成接口返回参数
     *
     * @return
     */
    public Map<String, Object> getMusic() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 20000);
        map.put("file_id", "123456788");
        return map;
    }
}
