package com.bjfu.notation_jh.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author jiahe
 * @create 2020/9/6 15:00
 */
@Setter
@Getter
public class BasicNotation {

    private Long id;
    private String uid;
    private Date createTime;
    private Date modifyTime;
    private String notationPic;
    private String notationName;
    private String notationGroup;
    private String notationStyle;
    private String notationTag;
    private String notationDesc;
    private String notationDownloadUrl;
    private int isPublic;
    private int weight;
}
