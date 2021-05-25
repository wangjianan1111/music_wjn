package com.bjfu.notation_jh.utils;

import com.bjfu.notation_jh.common.response.Result;

/**
 * @Author john
 * @create 2020/9/6 18:40
 */
public class ResultUtil {

    public static Result error(String flag, String message) {
        Result result = new Result();
        if ("success".equals(flag)) {
            result.setSuccess(true);
        }
        result.setMessage(message);
        return result;
    }

    public static Result success(String flag, String filename) {
        Result result = new Result();
        if ("success".equals(flag)) {
            result.setSuccess(true);
        }
        result.setFilename(filename);
        return result;
    }
}
