package com.cmy.controller;

import com.cmy.common.response.CommonResult;
import com.cmy.pojo.Response;
import com.cmy.pojo.UserInfo;
import com.cmy.pojo.UserVo;
import com.cmy.service.UserVoService;
import com.cmy.util.APIName;
import com.cmy.util.DrawImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class LoginController {

    @Autowired
    private UserVoService userVoService;
    @Autowired
    private DrawImage drawImage;

    //密码重置
    @APIName("初次登录密码修改")
    @PostMapping("/reset")
    @ResponseBody
    public Response<String> resetPwd(@RequestBody UserVo userVO, HttpServletRequest req){
        return userVoService.resetPassword(userVO.getPassword(),req);
    }
    /**
     * 用户申请账号
     */
    @APIName("申请账号")
    @PostMapping("/register")
    public CommonResult register(@RequestBody UserInfo userInfo){
        return userVoService.registerUser(userInfo);
    }

    @APIName("获取验证码")
    @GetMapping("/getVerifyCode")
    public void getImg(HttpServletRequest request,HttpServletResponse response){
        try {
            drawImage.creatRom(request,response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @APIName("修改默认密码")
    @PostMapping("/defaultP/{id}")
    private CommonResult editP(@PathVariable String id){
        return userVoService.editP(id);
    }
}
