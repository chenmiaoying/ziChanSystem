package com.cmy.interceptors;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.cmy.common.response.CommonResult;
import com.cmy.util.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/4/1 0001 17:37
 */
public class JWTInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        CommonResult commonResult;
        try{
            JWTUtil.myVerify(token);
            System.out.println("进入该拦截器");
            return true;
        }catch (SignatureVerificationException e){//签名不一致
            commonResult = CommonResult.errorCommonResult("签名不一致");
        }catch (TokenExpiredException e){//过期
            commonResult = CommonResult.errorCommonResult("令牌过期");
        }catch (AlgorithmMismatchException e){//算法不匹配
            commonResult = CommonResult.errorCommonResult("算法不匹配");
        }catch (InvalidClaimException e){//失效的payload异常
            commonResult = CommonResult.errorCommonResult("失效的payload异常");
        }catch (Exception e){
            commonResult = CommonResult.errorCommonResult("无效签名");
        }
        //将map转位json
        String json = new ObjectMapper().writeValueAsString(commonResult);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
        return false;
    }
}
