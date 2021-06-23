package com.cmy.auth;

import com.cmy.pojo.Response;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/5/5 0005 23:25
 */
@Component("myAuthenticationFailureHandler")
public class MyAuthenticationFailureHandler extends JSONAuthentication implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {
        Response<String> responseVo=null;
        if (e instanceof AccountExpiredException) {
            //账号过期
            responseVo=new Response<>("4001",null,null, "用户账号过期");
        } else if (e instanceof BadCredentialsException) {
            System.out.println(e.getMessage());
            //密码错误
            responseVo=new Response<>("4002",null,null, "用户密码错误");
        } else if (e instanceof CredentialsExpiredException) {
            //密码过期
            responseVo=new Response<>("4003",null,null, "用户密码过期");
        } else if (e instanceof DisabledException) {
            //账号不可用
            responseVo=new Response<>("4004",null,null, "账号不可用");
        } else if (e instanceof LockedException) {

            //账号锁定
            responseVo=new Response<>("4005",null,null, "账号锁定，请30分钟后再登陆，持续输错将会一直延长锁定时间");
        } else if (e instanceof InternalAuthenticationServiceException) {
            //用户不存在
            responseVo=new Response<>("4006",null,null, "用户不存在");
        }else{
            //其他错误
            responseVo=new Response<>("4007",null,null, "其他错误");
        }
        //输出
        this.WriteJSON(request, response, responseVo);
    }
}
