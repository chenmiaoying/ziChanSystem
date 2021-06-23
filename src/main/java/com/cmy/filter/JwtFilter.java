package com.cmy.filter;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.cmy.common.response.CommonResult;
import com.cmy.util.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/4/1 0001 20:47
 */
@WebFilter(urlPatterns = "/*")
public class JwtFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String token = request.getHeader("Authorization");
        if(!(request.getServletPath().equals("/login") || !(request.getServletPath().equals("/getVerifyCode"))
                || !(request.getServletPath().equals("/register"))) && token==null){
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Cache-Control", "no-cache");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().println(JSONObject.toJSONString(CommonResult.errorCommonResult("请先登录")));
            response.getWriter().flush();
        } else if(!(request.getServletPath().equals("/login") || !(request.getServletPath().equals("/getVerifyCode"))
                || !(request.getServletPath().equals("/register")))&&!verify(token)){
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Cache-Control", "no-cache");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().println(JSONObject.toJSONString(CommonResult.errorCommonResult("token验证不通过")));
            response.getWriter().flush();
        }else {
            chain.doFilter(req,res);
        }
    }
    boolean verify(String token){
        try{
            JWTUtil.myVerify(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
