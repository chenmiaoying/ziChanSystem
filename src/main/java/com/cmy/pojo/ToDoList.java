package com.cmy.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/6/2 0002 20:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToDoList {
    private String action;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
}
