package com.bjfu.notation_jh.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author jiahe
 * @create 2020/9/6 15:00
 */
@Setter
@Getter
public class MusicWork {
    private Long id;
    private String uid;
    private Long notationId;
    private Long singerId;
    private int remark;
    private String musicDownloadUrl;
    private String notationPic;
    private String musicName;
    private String musicId;

}
