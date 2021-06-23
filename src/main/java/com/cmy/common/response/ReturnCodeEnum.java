package com.cmy.common.response;

/**
 * @author 陈淼英
 * @date 2021/3/5
 */

public enum ReturnCodeEnum {
    SUCCESS(0),FAIL(-1),PARAMS_ERROR(3),DENIED_PERMISSION(4);
    private final Integer value;
    ReturnCodeEnum(Integer value) {
        this.value = value;
    }
    public Integer getValue(){
        return this.value;
    }
}