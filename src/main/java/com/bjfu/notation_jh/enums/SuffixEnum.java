package com.bjfu.notation_jh.enums;

import org.apache.commons.lang3.StringUtils;

/*
 * @author Arley
 */
public enum SuffixEnum {

    /**
     * pc端
     */
    PC_SUFFIX(1, "pc"),
    /**
     * 移动端
     */
    MOBILE_SUFFIX(2, "mobile");

    private String suffix;
    private Integer code;

    SuffixEnum(Integer code, String suffix) {
        this.code = code;
        this.suffix = suffix;
    }

    public static String getSuffix(Integer code) {
        if (null == code) {
            return StringUtils.EMPTY;
        }
        for (SuffixEnum suffixEnum : SuffixEnum.values()) {
            if (suffixEnum.code.equals(code)) {
                return suffixEnum.suffix;
            }
        }
        return StringUtils.EMPTY;
    }

    public static void main(String[] args) {
        System.out.println(SuffixEnum.getSuffix(1));
    }
}
