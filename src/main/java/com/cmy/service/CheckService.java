package com.cmy.service;

import com.cmy.common.response.CommonResult;
import com.cmy.pojo.BorrowCheck;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/4/9 0009 17:11
 */
public interface CheckService {

    /**
     * 添加借用审核工单
     * @param borrowCheck
     * @param request
     * @return
     */
    CommonResult addBCheck(BorrowCheck borrowCheck, HttpServletRequest request);

    /**
     * 获取当前用户的发起的工单
     * @param limit
     * @param offset
     * @param request
     * @param borrowCheck
     * @return
     */

    CommonResult getChecks(Integer limit ,Integer offset ,HttpServletRequest request,BorrowCheck borrowCheck);

    /**
     * 关闭工单
     * @return
     */
    CommonResult closeCheck(Integer checkId ,HttpServletRequest request);

    /**
     * 获得当前用户收到的别人的资产借用工单
     * @param limit
     * @param offset
     * @param request
     * @param borrowCheck
     * @return
     */
    CommonResult getBorrowChecks(Integer flag,Integer limit ,Integer offset ,HttpServletRequest request,BorrowCheck borrowCheck);


    /**
     * 同意与否该资产的借用工单
     * @param flag
     * @param id
     * @param b
     * @return
     */
    CommonResult allowCheck(Integer flag,Integer id,HttpServletRequest request,BorrowCheck b);

    /**
     * 领用
     * @param id
     * @return
     */
    CommonResult useAsset(Integer id);

    /**
     * 发起归还
     * @param id
     * @return
     */
    CommonResult backAsset(Integer id);

    /**
     * 确认归还
     * @param id
     * @return
     */
    CommonResult isBackAsset(Integer id,HttpServletRequest request);

    /**
     * 发起延期申请
     * @param borrowCheck
     * @return
     */
    CommonResult moreTime(BorrowCheck borrowCheck);

    /**
     * 下载领用单
     * @param request
     * @param response
     * @param id
     * @return
     */
    CommonResult downUse(HttpServletRequest request, HttpServletResponse response,Integer id);
}
