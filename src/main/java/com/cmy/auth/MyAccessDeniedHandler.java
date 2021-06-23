package com.cmy.auth;

import com.cmy.common.response.CommonResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 描述：权限不足处理类
 *
 * @author 陈淼英
 * @create 2021/5/6 0006 12:00
 */
@Component("myAccessDeniedHandler")
public class MyAccessDeniedHandler extends JSONAuthentication implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        CommonResult commonResult = CommonResult.errorCommonResult("权限不足，如有疑问请联系管理员");
        this.WriteJSON(request, response, commonResult );
    }
}
