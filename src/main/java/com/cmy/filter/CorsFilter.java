package com.cmy.filter;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/4/1 0001 20:47
 */
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebFilter(urlPatterns = "/*")
@Slf4j
public class CorsFilter implements Filter {
    private static final String OPTIONS = "OPTIONS";
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type,Token,Accept, Connection, User-Agent, Cookie, authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        //request.getHeader("Origin")
        if(OPTIONS.equalsIgnoreCase(request.getMethod())) {
            return;
        }
        chain.doFilter(req, res);
    }

}
