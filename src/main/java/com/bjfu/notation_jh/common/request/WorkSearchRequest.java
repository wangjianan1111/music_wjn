package com.bjfu.notation_jh.common.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WorkSearchRequest {

    private int pageNum;
    private int pageSize;

    private String uid;
    private Long notationId;
    private int remark;
    private String musicName;

}
