package com.bjfu.notation_jh.common.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author john
 * @create 2020/12/4 11:25
 */
@Getter
@Setter
public class RecognizeVO {

    private List<String> start;
    private List<String> name;
    private List<List<String>> note;
    private List<String> lycirs;

}
