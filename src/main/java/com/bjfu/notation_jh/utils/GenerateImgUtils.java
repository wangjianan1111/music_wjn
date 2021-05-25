package com.bjfu.notation_jh.utils;

import com.bjfu.notation_jh.model.NotationChange;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @Author john
 * @create 2020/12/15 18:48
 */
@Component
public class GenerateImgUtils {
    private BufferedImage image;
    private int imageWidth = 800;  //图片的宽度
    private int imageHeight = 1200; //图片的高度
    private int H_title = 50;     //头部高度
    private int notationWidth = 35;//音符图片宽度
    private int notationHeigth = 75;//音符图片高度


    //@Value("G:/000BJFU/skb/newskb")
    private static String notationUrl = "/profile/skb/newskb/";
//    private static String notationUrl = "G:/000BJFU/skb/newskb/";
    private static String images = "/profile/notationImages/";
//    private static String images = "G:/000BJFU/notationImages/";
    private static String retImages = "/notationImages/";

    //生成图片
    public String generateImage(List<String> notation, List<String> lycirs, NotationChange notationChange) {

        image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        //设置图片的背景色
        Graphics2D main = image.createGraphics();
        main.setColor(Color.white);
        main.fillRect(0, 0, imageWidth, imageHeight);

        //***********************页面头部
        Graphics title = image.createGraphics();
        //设置区域颜色
        title.setColor(new Color(255, 255, 255));
        //填充区域并确定区域大小位置
        title.fillRect(0, 0, imageWidth, H_title);
        //设置字体颜色，先设置颜色，再填充内容
        title.setColor(Color.black);
        //设置字体
        Font titleFont = new Font("宋体", Font.BOLD, 24);
        title.setFont(titleFont);
        title.drawString(notationChange.getTitle(), 270, (H_title) / 2 + 5);
        //处理keyandmeter
//        String key = notationChange.getKeyAndMeters();
//        String []keys = key.split(",");
        //1=bE,beat=4/4,temp=60
        title.drawString(notationChange.getKeyAndMeters(),0,(H_title) / 2 + 45);
        if (!"".equals(notationChange.getSubTitle())){
            title.drawString(notationChange.getSubTitle(),300,(H_title) / 2 + 45);
        }else{
            title.drawString(" ",300,(H_title) / 2 + 45);
        }
        if (!"".equals(notationChange.getWordsAndMusicBy())){
            title.drawString(notationChange.getWordsAndMusicBy(),500,(H_title) / 2 + 45);
        }else{
            title.drawString(notationChange.getWordsAndMusicBy(),500,(H_title) / 2 + 45);
        }

        //***********************插入中间广告图
        Graphics mainPic = image.getGraphics();
        Graphics lycirsPic = image.getGraphics();
        printImg(notation, lycirs,mainPic,title);
        mainPic.dispose();
        String uuid = String.valueOf(UUID.randomUUID());
        String img = images+ uuid +".jpg";
        createImage(img);

        return retImages+uuid +".jpg";
    }

    /**
     * 打印图片
     *
     * @param notation
     * @param lycirs
     */
    private void printImg(List<String> notation, List<String> lycirs,Graphics mainPic,Graphics title) {



        BufferedImage bimg = null;
        String img = "";
        int x = 0, y = H_title*2;
        for (int i = 0, j = 0; i < notation.size(); i++) {
            try {
                System.out.println(notationUrl + notation.get(i) + ".png");
                img = notationUrl + notation.get(i) + ".png";
                if ("|".equals(notation.get(i))) {
                    img = notationUrl + "_" + ".png";
                } else if (":|".equals(notation.get(i))) {
                    img = notationUrl + ".._" + ".png";
                } else if ("|:".equals(notation.get(i))) {
                    img = notationUrl + "_.." + ".png";
                } else if ("|]".equals(notation.get(i))) {
                    img = notationUrl + "]" + ".png";
                }

                bimg = javax.imageio.ImageIO.read(new java.io.File(img));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (bimg != null) {
                if (x > imageWidth-notationWidth) {
                    x=0;
                    y += (notationHeigth+5);
                }

                try {
                    mainPic.drawImage(javax.imageio.ImageIO.read(new java.io.File(img)), x, y, notationWidth, notationHeigth, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String c = notation.get(i);
                if (!"#".equals(c) && !"b".equals(c) && !"#b".equals(c) && !"|]".equals(c)
                         && !".".equals(c)) {
                    if ("/".equals(lycirs.get(j))||"|".equals(lycirs.get(j))) {
                        title.drawString(" ", x, y + notationHeigth);
                        j++;
                    } else {
                        if (j < lycirs.size()-1) {
                            if (",".equals(lycirs.get(j + 1)) || ".".equals(lycirs.get(j + 1)) ||
                                    "，".equals(lycirs.get(j + 1)) || "。".equals(lycirs.get(j + 1))) {
                                title.drawString(lycirs.get(j) + lycirs.get(j + 1), x, y + notationHeigth);
                                j++;
                            } else {
                                title.drawString(lycirs.get(j), x, y + notationHeigth);
                            }
                            j++;
                        }
                    }
                }
                x += notationWidth;
            }
        }
        title.drawString("123456@163.com",750,1100);

    }

    /**
     * 生成图片文件
     *
     * @param fileLocation
     */
    public void createImage(String fileLocation) {
        BufferedOutputStream bos = null;
        if (image != null) {
            try {
                FileOutputStream fos = new FileOutputStream(fileLocation);
                bos = new BufferedOutputStream(fos);

                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(bos);
                encoder.encode(image);
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {//关闭输出流
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
