package com.cmy.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {
    /**
     * 200:OK
     */
    private String code;
    private String token;

    private T data;

    private String msg;
}
