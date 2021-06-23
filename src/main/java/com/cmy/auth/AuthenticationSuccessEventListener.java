package com.cmy.auth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cmy.dao.AllApiMapper;
import com.cmy.dao.SysLogMapper;
import com.cmy.dao.UserMapper;
import com.cmy.pojo.*;
import com.cmy.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/6/3 0003 0:11
 */
@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent authenticationSuccessEvent) {
        SecurityUser userDetails = (SecurityUser) authenticationSuccessEvent.getAuthentication().getPrincipal();

        String userId = userDetails.getUsername();
        UserLogin userLogin = userMapper.selectById(userId);
        userLogin.setActiving(1);
        userLogin.setActive(1);
        userLogin.setFailsCount(0);
        userMapper.updateById(userLogin);
    }
}
