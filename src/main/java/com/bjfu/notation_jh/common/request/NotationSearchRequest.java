package com.bjfu.notation_jh.common.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NotationSearchRequest {

    private String notationName;
    private String notationGroup;
    private String notationStyle;
    private String notationTag;
    private int pageNum;
    private int pageSize;
    private String uid;
    private int isPublic;



}
