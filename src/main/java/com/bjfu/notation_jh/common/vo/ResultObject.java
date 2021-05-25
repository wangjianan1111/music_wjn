package com.bjfu.notation_jh.common.vo;

import com.bjfu.notation_jh.model.User;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author john
 * @create 2020/9/6 16:21
 */
@Data
public class ResultObject implements Serializable {

    /**
     * 错误码:SUCCESS|FAIL
     */
    private String errorCode;

    private User user;
}
