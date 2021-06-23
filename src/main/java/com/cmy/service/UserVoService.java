package com.cmy.service;

import com.cmy.common.response.CommonResult;
import com.cmy.pojo.Response;
import com.cmy.pojo.UserInfo;
import com.cmy.pojo.UserVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface UserVoService {
//    /**
//     * 登录
//     * @param userVo
//     * @param req
//     * @param resp
//     * @return
//     */
//    boolean login(UserVo userVo, HttpServletRequest req,HttpServletResponse resp);
//
//    /**
//     * 判断是否是第一次登录
//     *
//     * @param req
//     * @return
//     */
//    boolean isFirst(HttpServletRequest req);
//
//    /**
//     * 最后的登录入口
//     * @param userVO
//     * @param req
//     * @param response
//     * @return
//     */
//    Response<String> commonLogin(UserVo userVO, HttpServletRequest req, HttpServletResponse response);

    /**
     * 重置密码
     * @param newPwd
     * @param req
     * @return
     */
    Response<String> resetPassword(String newPwd, HttpServletRequest req);

    /**
     * 申请账号
     * @param userInfo
     * @return
     */
    CommonResult registerUser(UserInfo userInfo);

    /**
     * 修改默认密码
     * @param id
     * @return
     */
    CommonResult editP(String id);
}
