package com.boot.store.vo;

import lombok.Data;

/**
 * @author aiwei
 */
@Data
public class ResultVo<T> {

    private Integer code;

    private String msg;

    private T data;
}
