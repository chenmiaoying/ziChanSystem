package com.cmy.auth.authority;

import com.cmy.dao.UserInfoMapper;
import com.cmy.dao.UserMapper;
import com.cmy.dao.UserRoleMapper;
import com.cmy.pojo.SecurityUser;
import com.cmy.pojo.UserLogin;
import com.cmy.pojo.UserRole;
import com.cmy.service.impl.UserDetailsServiceImpl;
import com.cmy.util.JWTUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/5/22 0022 21:42
 */
@Component("myAuthenticationFilter")
public class MyAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    private final UserDetailsServiceImpl userDetailsService;

    protected MyAuthenticationFilter(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(request.getServletPath().equals("/login")||request.getServletPath().equals("/register")
                ||request.getServletPath().equals("/getVerifyCode")) {
            filterChain.doFilter(request, response);
            return;
        }
        StopWatch stopWatch = new StopWatch();
        try {
            stopWatch.start();

            // 前后端分离情况下，前端登录后将token储存在cookie中，每次访问接口时通过token去拿用户权限
            String token = request.getHeader("Authorization");

            if (StringUtils.isNotBlank(token)) {
                // 检查token
                try{
                    JWTUtil.myVerify(token);
                    String userId = JWTUtil.getUserId(token);
                    UserLogin userLogin=userMapper.selectById(userId);
                    if(userLogin.getActiving()==0){
                        throw new AccessDeniedException("非法获取Token，非法访问！！");
                    }
                    Integer roleId = userInfoMapper.selectById(userId).getRoleId();
                    UserRole userRole = userRoleMapper.selectById(roleId);
                    List<UserRole> list=new ArrayList<>();
                    list.add(userRole);
                    SecurityUser securityUser=new SecurityUser(userLogin,list,true);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
                    // 全局注入角色权限信息和登录用户基本信息
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }catch (Exception e){
                    e.printStackTrace();
                    throw new AccessDeniedException("Token验证未通过，请重新登录！");
                }
            }
            filterChain.doFilter(request, response);
        } finally {
            stopWatch.stop();
        }
    }
}
