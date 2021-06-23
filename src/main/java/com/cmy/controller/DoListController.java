package com.cmy.controller;

import com.cmy.common.response.CommonResult;

import com.cmy.pojo.DoList;
import com.cmy.service.DoListService;
import com.cmy.util.APIName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 描述：代办事项
 *
 * @author 陈淼英
 * @create 2021/4/29 0029 18:38
 */
@RestController
public class DoListController {
    @Autowired
    private DoListService doListService;

    @APIName("获取代办事项")
    @PostMapping("/doList")
    public CommonResult getCheckPages(@RequestBody DoList doList,
                                      HttpServletRequest request){
        return doListService.getDolist(request,doList);
    }
}
