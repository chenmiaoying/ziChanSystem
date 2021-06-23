package com.cmy.filter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cmy.dao.AllApiMapper;
import com.cmy.dao.SysLogMapper;
import com.cmy.dao.UserMapper;
import com.cmy.pojo.AllApi;
import com.cmy.pojo.SysLog;
import com.cmy.pojo.UserBehavior;
import com.cmy.pojo.UserLogin;
import com.cmy.service.LogService;
import com.cmy.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;


/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/4/1 0001 20:47
 */
@WebFilter(urlPatterns = "/*")
public class LogFilter implements Filter {

    @Autowired
    private LogService logService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AllApiMapper allApiMapper;
    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String token = request.getHeader("Authorization");
        UserBehavior record = isRecord(request.getServletPath(), request.getMethod());
        if(request.getServletPath().equals("/login")&&request.getMethod().equals("POST")) {
            chain.doFilter(req, res);
            HttpServletResponse response = (HttpServletResponse)res;
            if(response.getStatus() == 200) {
                // 登录成功
                insetIntoLog(response.getHeader("token"),request,record);
            }
        }else if(token !=null && !token.equals("") && record != null && verifyToken(token)){
            insetIntoLog(token,request,record);
            chain.doFilter(req, res);
        } else {
            chain.doFilter(req, res);
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

    UserLogin getLoginUser(String token) {
        String userId = JWTUtil.getUserId(token);
        return userMapper.selectById(Integer.parseInt(userId));
    }

    String getClassNameAndMethodName(String url,String type) {
        AllApi allApi = allApiMapper.selectOne(new QueryWrapper<AllApi>().eq("api_url", url).eq("api_type", type));
        return allApi.getApiClassName()+"."+allApi.getApiMethodName()+"()";
    }

    boolean verifyToken(String token) {
        try{
            JWTUtil.myVerify(token);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    void insetIntoLog(String token, HttpServletRequest request, UserBehavior record) {
        UserLogin loginUser = getLoginUser(token);
        SysLog sysLog = new SysLog();
        sysLog.setUserId(Long.parseLong(loginUser.getId().toString()));
        sysLog.setCreateTime(new Date());
        sysLog.setUsername(loginUser.getUsername());
        sysLog.setIp(getIpAddr(request));
        sysLog.setType(request.getMethod());
        sysLog.setOperation(record.getApiDescribe());
        sysLog.setMethod(getClassNameAndMethodName(request.getServletPath(),request.getMethod()));
        sysLogMapper.insert(sysLog);
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