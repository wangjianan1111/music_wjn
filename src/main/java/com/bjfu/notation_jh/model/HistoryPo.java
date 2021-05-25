package com.bjfu.notation_jh.model;

import lombok.*;

import java.io.Serializable;

/**
 * @author Arley
 */
@Data
@ToString
@EqualsAndHashCode
public class HistoryPo implements Serializable {

    private static final long serialVersionUID = 6219395110231623092L;
    private Integer code;
    private Long userId;
    private Long itemId;
    private String name;
}
