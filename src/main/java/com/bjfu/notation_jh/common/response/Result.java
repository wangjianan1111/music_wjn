package com.bjfu.notation_jh.common.response;

import com.bjfu.notation_jh.model.TmpNotation;
import com.bjfu.notation_jh.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author john
 * @create 2020/9/6 18:40
 */
@Setter
@Getter
public class Result {

    private boolean isSuccess = true;

    private String message = "";

    private String filename = "";

    private List list;

    private int totalPages;

    private User user;

    private int statusCode;

    private String str;

    private TmpNotation tmpNotation;

    private Object object;

    public Result() {
    }

    public Result(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
