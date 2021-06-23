package com.cmy.auth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cmy.dao.AllApiMapper;
import com.cmy.dao.SysLogMapper;
import com.cmy.dao.UserMapper;
import com.cmy.pojo.*;
import com.cmy.service.LogService;
import com.cmy.util.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * 描述：登录成功操作
 *
 * @author 陈淼英
 * @create 2021/5/5 0005 22:47
 */
@Component("myAuthenticationSuccessHandler")
public class MyAuthenticationSuccessHandler extends JSONAuthentication implements AuthenticationSuccessHandler {


    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SysLogMapper sysLogMapper;
    @Autowired
    private AllApiMapper allApiMapper;
    @Autowired
    private LogService logService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        //获取当前的登录用户
        Object principal = authentication.getPrincipal();
        UserDetails userDetails = null;
        if(principal instanceof UserDetails){
            userDetails = (UserDetails)principal;
        }
        UserLogin userLogin = userMapper.selectById(userDetails.getUsername());
        insetIntoLog(userLogin,request,isRecord("/login","POST"));
        //构建token
        String token= JWTUtil.getToken(userLogin);
        Response<String> responseVo=null;
        if(isFirst(userLogin)){
            response.setStatus(200);
            responseVo = new Response<>("202", token,userLogin.getUsername(), "用户第一次登陆需要求改密码");
        }else {
            response.setStatus(200);
            responseVo = new Response<>("200", token,userLogin.getUsername(), "用户登录成功");
        }
        //将map转位json
        this.WriteJSON(request,response,responseVo);
    }
    public boolean isFirst(UserLogin userLogin) {
        if (userLogin.getStatus()==1){
            return true;
        }else{
            return false;
        }
    }
    UserBehavior isRecord(String url, String type) {
        UserBehavior userBehavior = logService.getUserBehaviorConfigByUrlAndType(url, type);
        if(userBehavior == null) {
            return null;
        } else {
            return userBehavior;
        }
    }
    void insetIntoLog(UserLogin userLogin, HttpServletRequest request, UserBehavior record) {
        SysLog sysLog = new SysLog();
        sysLog.setUserId(Long.parseLong(userLogin.getId().toString()));
        sysLog.setCreateTime(new Date());
        sysLog.setUsername(userLogin.getUsername());
        sysLog.setIp(getIpAddr(request));
        sysLog.setType(request.getMethod());
        sysLog.setOperation(record.getApiDescribe());
        sysLog.setMethod(getClassNameAndMethodName(request.getServletPath(),request.getMethod()));
        sysLogMapper.insert(sysLog);
    }
    String getClassNameAndMethodName(String url,String type) {
        AllApi allApi = allApiMapper.selectOne(new QueryWrapper<AllApi>().eq("api_url", url).eq("api_type", type));
        return allApi.getApiClassName()+"."+allApi.getApiMethodName()+"()";
    }
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            String localIp = "127.0.0.1";
            String localIpv6 = "0:0:0:0:0:0:0:1";
            if (ipAddress.equals(localIp) || ipAddress.equals(localIpv6)) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                    ipAddress = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        String ipSeparate = ",";
        int ipLength = 15;
        if (ipAddress != null && ipAddress.length() > ipLength) {
            if (ipAddress.indexOf(ipSeparate) > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(ipSeparate));
            }
        }
        return ipAddress;
    }
}
