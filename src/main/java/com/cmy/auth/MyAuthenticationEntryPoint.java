package com.cmy.auth;

import com.cmy.common.response.CommonResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 描述：匿名用户访问无权限资源时的异常
 *
 * @author 陈淼英
 * @create 2021/5/6 0006 0:21
 */
@Component("myAuthenticationEntryPoint")
public class MyAuthenticationEntryPoint extends JSONAuthentication implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        CommonResult commonResult = CommonResult.errorCommonResult("请先登录");
        this.WriteJSON(request,response,commonResult);
    }
}
