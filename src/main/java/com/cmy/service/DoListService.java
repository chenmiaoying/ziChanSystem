package com.cmy.service;

import com.cmy.common.response.CommonResult;
import com.cmy.pojo.DoList;

import javax.servlet.http.HttpServletRequest;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/4/29 0029 18:53
 */

public interface DoListService {
    CommonResult getDolist(HttpServletRequest request , DoList doList);
}
