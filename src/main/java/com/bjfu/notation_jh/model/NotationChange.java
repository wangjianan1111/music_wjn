package com.bjfu.notation_jh.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author jiahe
 * @create 2020/9/6 15:00
 */
@Setter
@Getter
public class NotationChange {

    private Long id;
    private String uid;
    private Long notationId;
    private String content;
    private Date createTime;
    private Date modifyTime;
    private Long tmpId;
    private String title;
    private String subTitle;
    private String keyAndMeters;
    private String wordsAndMusicBy;

}
