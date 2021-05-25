package com.bjfu.notation_jh.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author jiahe
 * @create 2020/9/6 15:00
 */
@Getter
@Setter
public class User {
    private String id;
    private String phone;
    private String loginId;
    private String password;
    private Date createTime;
    private Date modifyTime;
    private String nickName;
    private String email;
    private String faceImage;
    private String birthday;
    private String sex;
}
