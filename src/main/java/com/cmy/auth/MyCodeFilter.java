package com.cmy.auth;

import com.alibaba.fastjson.JSONObject;
import com.cmy.common.response.CommonResult;
import com.cmy.pojo.Response;
import com.cmy.util.APIName;
import com.cmy.util.DrawImage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：验证码验证
 *
 * @author 陈淼英
 * @create 2021/5/30 0030 18:19
 */
@Component("myCodeFilter")
public class MyCodeFilter extends OncePerRequestFilter {
    @Autowired
    private DrawImage drawImage;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Response<String> responseVo = null;
        String uuid = request.getParameter("uuid");
        String code = request.getParameter("code");
        Boolean flag = false;
        String method = request.getMethod();
        String url = request.getServletPath();

        if ("POST".equals(method) &&
                "/login".equals(url)) {

            flag = drawImage.verifyCaptchaStatus(uuid, code);
            if (!flag) {
                //验证码不通过
                responseVo = new Response<>("3000", null, null, "验证码错误，请重新输入");
                //输出
                response.setContentType("application/json;charset=UTF-8");
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Access-Control-Allow-Method", "POST,GET");
                //输出JSON
                PrintWriter out = response.getWriter();
                out.write(new ObjectMapper().writeValueAsString(responseVo));
                out.flush();
                out.close();
                return;
            } else {
                filterChain.doFilter(request, response);
            }
        } else if ("POST".equals(method) && "/register".equals(url)) {

            flag = drawImage.verifyCaptchaStatus(uuid, code);

            if (!flag) {
                //验证码不通过
                System.out.println(uuid);
                System.out.println(code);
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Cache-Control", "no-cache");
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                response.getWriter().println(JSONObject.toJSONString(CommonResult.errorCommonResult("验证码错误，请重新输入")));
                response.getWriter().flush();
                return;
            } else {
                filterChain.doFilter(request, response);
            }

        } else {
            filterChain.doFilter(request, response);
        }
    }
}

//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//
//        HttpServletRequest request=(HttpServletRequest)servletRequest;
//        HttpServletResponse response=(HttpServletResponse) servletResponse;
//        Response<String> responseVo=null;
//        String uuid = request.getParameter("uuid");
//        String code =request.getParameter("code");
//        Boolean flag =false;
//        String method = request.getMethod();
//        String url=request.getServletPath();
//
//        if("POST".equals(method)&&
//                "/login".equals(url)){
//
//            flag = drawImage.verifyCaptchaStatus(uuid, code);
//            if(!flag){
//                //验证码不通过
//                responseVo=new Response<>("3000",null,null, "验证码错误，请重新输入");
//                //输出
//                response.setContentType("application/json;charset=UTF-8");
//                response.setHeader("Access-Control-Allow-Origin", "*");
//                response.setHeader("Access-Control-Allow-Method", "POST,GET");
//                //输出JSON
//                PrintWriter out = response.getWriter();
//                out.write(new ObjectMapper().writeValueAsString(responseVo));
//                out.flush();
//                out.close();
//                return;
//            }else {
//                filterChain.doFilter(request,response);
//            }
//        }else if("POST".equals(method) && "/register".equals(url)){
//
//            flag = drawImage.verifyCaptchaStatus(uuid, code);
//
//            if(!flag){
//                //验证码不通过
//                System.out.println(uuid);
//                System.out.println(code);
//                response.setHeader("Access-Control-Allow-Origin", "*");
//                response.setHeader("Cache-Control", "no-cache");
//                response.setCharacterEncoding("UTF-8");
//                response.setContentType("application/json");
//                response.getWriter().println(JSONObject.toJSONString(CommonResult.errorCommonResult("验证码错误，请重新输入")));
//                response.getWriter().flush();
//                return;
//            }else {
//                filterChain.doFilter(request,response);
//            }
//
//        } else {
//            filterChain.doFilter(request,response);
//        }
//    }
//}
