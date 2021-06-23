package com.cmy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述：返回首页的3个汇总实体类
 *
 * @author 陈淼英
 * @create 2021/4/30 0030 13:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnShou {
    //从上到下，从左到右
    private Integer one;
    private Integer two;
    private Integer three;
    private Integer flag;
}
