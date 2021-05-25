package com.bjfu.notation_jh.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author john
 * @create 2020/12/7 13:54
 */
@Setter
@Getter
public class MusicSynthesis {

    private long id;
    private String uid;
    private long singer;
    private String musicName;
    private String musicType;
    private int isPublic;
    private String imageUrl;
    private int finished;
    private Date createTime;
    private String musicId;
    private Long notationId;

}
