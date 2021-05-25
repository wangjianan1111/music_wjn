package com.bjfu.notation_jh.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author john
 * @create 2020/9/6 18:40
 */
@Getter
public enum ResultEnum {

    SUCCESS("SUCCESS", "成功"),
    FAIL("FAIL", "失败");

    ResultEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private String code;
    /**
     * 描述
     */
    private String desc;
}
