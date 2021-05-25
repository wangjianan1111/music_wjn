package com.bjfu.notation_jh.utils;

import com.alibaba.fastjson.JSONObject;
import com.bjfu.notation_jh.common.response.RecognizeVO;
import com.bjfu.notation_jh.model.NotationChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author john
 * @create 2020/12/3 23:53
 */
@Component
public class ParamChangeUtils {

    //    @Value("G:/000BJFU/")
    private static String fileUrl = "/profile/";
//    private static String fileUrl = "G:/000BJFU/";

    private static final Logger LOGGER = LoggerFactory.getLogger(ParamChangeUtils.class);

    private static int sp = 18;

    /**
     * 识谱接口回传参数转换键盘按键
     *
     * @param jsonObject
     * @return
     */
    public NotationChange changeRecognieParams2Keyboard(JSONObject jsonObject) {
        NotationChange notationChange = new NotationChange();
        RecognizeVO vo = JSONObject.toJavaObject(jsonObject, RecognizeVO.class);
        List<List<String>> note = vo.getNote();
        List<String> lycirs = vo.getLycirs();
        List<String> name = vo.getName();
        List<String> start = vo.getStart();
        String title = "";
        for (int i = 0; i < name.size(); i++) {
            title += name.get(i);
        }
        notationChange.setTitle(title);
        String str1 = "1=";
        String str2 = "beat=" + start.get(start.size() - 3) + "/" + start.get(start.size() - 2);
        String str3 = "temp=" + start.get(start.size() - 1);
        for (int i = start.size() - 4; i >= 0; i--) {
            str1 += start.get(i);
        }
        notationChange.setKeyAndMeters(str1 + "," + str2 + "," + str3);
        String content = "";
        String len1 = "";
        String len2 = "";
        int count = 0;
        for (int i = 0; i < note.size(); i++) {
            //如果超过18个字符就换行
            if (count >= 25) {
                content += len1;
                content += "<br>";
                content += len2;
                content += "<br>";
                len1 = "";
                len2 = "";
                count = 0;
            }
//            if (count != 0 && count % 18 == 0) {
//                content += len1;
//                content += "<br>";
//                content += len2;
//                content += "<br>";
//                len1 = "";
//                len2 = "";
//            }
            List<String> subNote = note.get(i);
            len1 += subNote.get(0);
            count++;
            //高低音
            if (!"".equals(subNote.get(1))) {
                if (subNote.get(1).charAt(0) == '-' && subNote.get(1).charAt(1) != '0') {
                    for (int j = 0; j < Integer.valueOf(String.valueOf(subNote.get(1).charAt(1))); j++) {
                        len1 += "d";
                        count++;
                    }
                } else if (!"0".equals(subNote.get(1))) {
                    for (int j = 0; j < Integer.valueOf(subNote.get(1)); j++) {
                        len1 += "g";
                        count++;
                    }
                }
            }
            if (!"".equals(subNote.get(2))) {
                //下划线
                if (!"0".equals(subNote.get(2))) {
                    for (int j = 0; j < Integer.valueOf(subNote.get(2)); j++) {
                        len1 += "_";
                        count++;
                    }
                }
            }
            //歌词
            if ("|".equals(subNote.get(0))) {
                len2 += "|";
            } else if (!".".equals(subNote.get(0)) && "+".equals(lycirs.get(i))) {
                len2 += "/";
            } else if (":".equals(subNote.get(0))) {
//                len2 += "";
            } else {
                if ("+".equals(lycirs.get(i))) {
                    //什么也不干
                } else if ("*".equals(lycirs.get(i)) || "-".equals(lycirs.get(i)) || "".equals(lycirs.get(i))) {
                    len2 += "/";
                } else {
                    len2 += lycirs.get(i);
                }
            }
        }
        content += len1;
        content += "<br>";
        content += len2;
        notationChange.setContent(content);

        return notationChange;

    }

    /**
     * 识谱接口回传参数转换歌声合成参数
     *
     * @param jsonObject
     * @return 地址
     */
    public String changeRecognieParams2Synthesis(JSONObject jsonObject) {
        RecognizeVO vo = JSONObject.toJavaObject(jsonObject, RecognizeVO.class);
        /**
         * ver=1.0
         * 1=bE
         * beat=4/4
         * temp=60
         */
        String str1 = "ver=1.0";
        List<String> start = vo.getStart();
//        String str2 = "1=" + start.get(1) + start.get(0);
//        String str3 = "beat=" + start.get(2) + "/" + start.get(3);
//        String str4 = "temp=" + start.get(4);

        String str2 = "1=";
        String str3 = "beat=" + start.get(start.size() - 3) + "/" + start.get(start.size() - 2);
        String str4 = "temp=" + start.get(start.size() - 1);
        for (int i = start.size() - 4; i >= 0; i--) {
            str2 += start.get(i);
        }
        //不需要name

        //音符
        List<List<String>> note = vo.getNote();
        //歌词
        List<String> lycirs = vo.getLycirs();
        //从第一个歌词开始
        int first = 0;
        for (int i = 0; i < lycirs.size(); i++) {
            if (!"".equals(lycirs.get(i)) && !"+".equals(lycirs.get(i)) && !"*".equals(lycirs.get(i))) {
                first = i;
                break;
            }
        }
        List<String> musicStr = new ArrayList<>();
        String str5 = ""; //简谱
        String str6 = ""; //音长
        String str7 = ""; //歌词
        for (int i = first; i < note.size(); i++) {
            List<String> tmp = note.get(i);
            if ("|".equals(tmp.get(0))) {
                musicStr.add(str5 + "\r\n");
                musicStr.add(str6 + "\r\n");
                musicStr.add(str7 + "\r\n\r\n");
                str5 = "";
                str6 = "";
                str7 = "";
                continue;
            }
            if ("(".equals(tmp.get(0))||")".equals(tmp.get(0))){
                continue;
            }
            str5 += buildStr(tmp);
            str6 += buildStr2(tmp);
            str7 += buildStr3(lycirs.get(i));

        }
        musicStr.add(str5 + "\r\n");
        musicStr.add(str6 + "\r\n");
        musicStr.add(str7 + "\r\n\r\n");
        //musicStr写入到txt文档中，并返回url
        String fileName = UUID.randomUUID().toString();
        File file = new File(fileUrl + fileName + ".txt");
        try (FileOutputStream fop = new FileOutputStream(file, true)) {

            // if file doesn't exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            fop.write((str1 + "\r\n").getBytes());
            fop.write((str2 + "\r\n").getBytes());
            fop.write((str3 + "\r\n").getBytes());
            fop.write((str4 + "\r\n").getBytes());

            //将musicStr中的字符串写入到文档中
            for (String sss : musicStr) {
                fop.write(sss.getBytes());
            }
            fop.flush();
            fop.close();

            LOGGER.info(fileName + ".txt output has Done");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileUrl + fileName + ".txt";
    }

    /**
     * build歌词
     *
     * @param s
     * @return
     */
    private String buildStr3(String s) {
        String str = "";
        if ("+".equals(s) || "*".equals(s) || "".equals(s)) {
            str += "x";
        } else {
            str += s;
        }
        return str + " ";

    }

    /**
     * build音长
     *
     * @param tmp
     * @return
     */
    private String buildStr2(List<String> tmp) {
        String str = "";
        if ("0".equals(tmp.get(2))||"".equals(tmp.get(2))) {
            str += "x";
        } else if ("1".equals(tmp.get(2))) {
            str += "-";
        } else if ("2".equals(tmp.get(2))) {
            str += "=";
        }
        return str + " ";
    }

    /**
     * build音符
     *
     * @param tmp
     * @return
     */
    private String buildStr(List<String> tmp) {
        String str = "";
        String s = tmp.get(0);
        if (".".equals(s) || "-".equals(s)) {
            str += s;
        } else {
            if ("0".equals(tmp.get(1))) {
                str += s;
            } else if ("-1".equals(tmp.get(1))) {
                str += ("." + s);
            } else if ("-2".equals(tmp.get(1))) {
                str += (".." + s);
            } else if ("-3".equals(tmp.get(1))) {
                str += ("..." + s);
            } else if ("1".equals(tmp.get(1))) {
                str += (s + ".");
            } else if ("2".equals(tmp.get(1))) {
                str += (s + "..");
            } else if ("3".equals(tmp.get(1))) {
                str += (s + "...");
            } else if ("".equals(tmp.get(1))) {
                str += "x";
            }
        }
        return str + " ";
    }

    /**
     * 键盘转换为合成参数
     * 键盘-》识谱接口参数-》（已有）歌声合成参数
     *
     * @param param
     * @return
     */
    public String changeKeyboard2Synthesis(NotationChange param) {
        //构造简谱和歌词list
        List<String> notation = new ArrayList<>();
        List<String> prelycirs = new ArrayList<>();
        buildNotationAndLycirs(notation, prelycirs, param);
        //分别对简谱和歌词进行转化
        RecognizeVO vo = new RecognizeVO();
        List<String> start = new ArrayList<>();
        List<String> name = new ArrayList<>();
        List<List<String>> note = new ArrayList<>();
        List<String> lycirs = new ArrayList<>();

        /**
         * start1 = ['E', 'b', '4', '4', '60']
         * name1 = ['阿', '果', '吉', '曲']
         * 1=bE,beat=4/4,temp=60
         */
        String key = param.getKeyAndMeters();
        String[] keys = key.split(",");
        for (int i = keys[0].length() - 1; i >= 2; i--) {
            start.add(String.valueOf(keys[0].charAt(i)));
        }
        start.add(String.valueOf(keys[1].charAt(keys[1].length() - 3)));
        start.add(String.valueOf(keys[1].charAt(keys[1].length() - 1)));
        start.add(String.valueOf(keys[2].substring(5)));
        String title = param.getTitle();
        for (int i = 0; i < title.length(); i++) {
            name.add(String.valueOf(title.charAt(i)));
        }

        //简谱
        /**
         * # 音符，一个音符用4个长度表示
         * # 第一个是基本音（用字符0-7表示）
         * # 第二个表示是否有高低音，高音用字符（1,2,3）表示，低音用字符（-1,-2,-3）表示
         * # 第三个表示是否有下划线，数字多少就代表有多少下划线
         * # 第四个表示是否有滑音
         * # 其他符号，后面三个直接用空表示, "." 和 "|" 和 "()" 均为英文下的标点符号
         *
         * 3_5_6_6__5__ | 6_.3__2 | 3_5_6_6__5__ |
         * 跑马溜溜的 | 山/上, | 一朵溜溜的 |
         */
        int k = 0;
        for (int i = 0; i < notation.size(); i++) {
            List<String> sub = new ArrayList<>();
            if (i == 273) {
                System.out.println(i);
            }
            System.out.println(i);
            String tmp = notation.get(i);
            sub.add(String.valueOf(tmp.charAt(0)));
            int j = 1;
            int count = 0;
            char c = 0;
            if (j < tmp.length()) {
                c = tmp.charAt(j);
            }
            while (j < tmp.length() && (tmp.charAt(j) == 'g' || tmp.charAt(j) == 'd')) {
                j++;
                count++;
            }
            if (c == 'g') {
                sub.add(String.valueOf(count));
            } else if (c == 'd') {
                sub.add("-" + String.valueOf(count));
            } else {
                sub.add("0");
            }
            int count1 = 0;
            while (j < tmp.length() && tmp.charAt(j) == '_') {
                j++;
                count1++;
            }
            sub.add(String.valueOf(count1));
            sub.add("0"); //特殊符号暂时输入为0
            note.add(sub);
            if (k == 253) {
                System.out.println(k);
            }
            if (tmp.charAt(0) == '0') {
                lycirs.add("*");
            } else {
                if (tmp.charAt(0) == '.') {
                    lycirs.add("+");
                    k--;
                } else if ("/".equals(prelycirs.get(k)) || "|".equals(prelycirs.get(k))) {
                    lycirs.add("");
                } else {
                    lycirs.add(prelycirs.get(k));
                }
            }
            k++;
            if (",".equals(prelycirs.get(k)) || "。".equals(prelycirs.get(k)) || "，".equals(prelycirs.get(k))) {
                k++;
            }
        }

        vo.setStart(start);
        vo.setName(name);
        vo.setNote(note);
        vo.setLycirs(lycirs);
        String besnString = JSONObject.toJSONString(vo);
        JSONObject jsonObject = JSONObject.parseObject(besnString);
        return changeRecognieParams2Synthesis(jsonObject);
    }

    /**
     * 转换前端参数为图片
     *
     * @param notationChange
     * @return 图片地址
     */
    public String changeKeyboard2Img(NotationChange notationChange) {

        //构造简谱和歌词list
        List<String> notation = new ArrayList<>();
        List<String> lycirs = new ArrayList<>();
        buildNotationAndLycirs(notation, lycirs, notationChange);
        GenerateImgUtils imgUtils = new GenerateImgUtils();
        String imgurl = imgUtils.generateImage(notation, lycirs, notationChange);
        return imgurl;
    }

    private void buildNotationAndLycirs(List<String> notation, List<String> lycirs, NotationChange notationChange) {

        String content = notationChange.getContent();

        String[] tmp = content.split("<br>");
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < tmp.length; i++) {
            //替换中文空格为英文空格
            tmp[i] = tmp[i].replace((char) 12288, ' ');
            tmp[i] = tmp[i].trim();
            String c = tmp[i].trim();
            char s = tmp[i].charAt(0);
            if (s == '|') {
                s = tmp[i].charAt(1);
            }
            if ((s >= '0' && s < '8') || s == '#' || s == 'b' || '(' == s || ')' == s || '-' == s || '.' == s) {
                sb.append(tmp[i]);
            } else {
                sb2.append(tmp[i]);
            }
        }
        //定位图片并输出
        String temp = "";
        int len = sb.length() - 1;
        for (int i = 0; i < sb.length() - 1; i++) {
            char c = sb.charAt(i);
            if ('#' == c && i < len && 'b' == sb.charAt(i + 1)) {
                notation.add("#b");
                ++i;
                continue;
            } else if ('#' == c) {
                notation.add("#");
                continue;
            } else if ('b' == c) {
                notation.add("b");
                continue;
            } else if ('.' == c) {
                notation.add(".");
                continue;
            } else if ('(' == c) {
                notation.add("(");
                continue;
            } else if (')' == c) {
                notation.add(")");
                continue;
            } else if (':' == c && i < len && '|' == sb.charAt(i + 1)) {
                notation.add(":|");
                i++;
                continue;
            } else if ('|' == c && i < len && ':' == sb.charAt(i + 1)) {
                notation.add("|:");
                i++;
                continue;
            } else if ('|' == c && i < len && ']' == sb.charAt(i + 1)) {
                notation.add("|]");
                i++;
                continue;
            } else if ('|' == c) {
                notation.add("|");
                continue;
            } else if ('-' == c) {
                notation.add("-");
                continue;
            } else if (12288 == c || 160 == c || 32 == c) {
                continue;
            } else {
                temp += c;
                while (i < len && (sb.charAt(i + 1) == 'g' || sb.charAt(i + 1) == 'd' || sb.charAt(i + 1) == ',' || sb.charAt(i + 1) == '\'' || sb.charAt(i + 1) == '_')) {
                    temp += sb.charAt(i + 1);
                    i++;
                }
                notation.add(temp);
                temp = "";

            }
        }
        for (int i = 0; i < sb2.length(); i++) {
            char c = sb2.charAt(i);
            if (12288 == c || 160 == c || 32 == c) {
                continue;
            } else {
                lycirs.add(String.valueOf(c));
            }
        }


    }

//    public static void main(String[] args) {
//        String a = " ji ";
//        a = a.trim();
//        System.out.println(a);
//        String[] b = {" 跑马溜溜", " 积极 "};
//        b[0] = b[0].trim();
//        b[1] = b[1].trim();
//
//        System.out.println(b[0]);
//        System.out.println(b[1]);
//    }

}
