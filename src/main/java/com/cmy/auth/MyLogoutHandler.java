package com.cmy.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 描述：登出处理类
 *
 * @author 陈淼英
 * @create 2021/5/6 0006 12:13
 */
@Component("myLogoutHandler")
public class MyLogoutHandler extends JSONAuthentication implements LogoutHandler {
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String headerToken = request.getHeader("Authorization");
        //
        if (!StringUtils.isEmpty(headerToken)) {
            //postMan测试时，自动假如的前缀，要去掉。
            //String token = headerToken.replace("Bearer", "").trim();
            SecurityContextHolder.clearContext();
        }
    }

}
