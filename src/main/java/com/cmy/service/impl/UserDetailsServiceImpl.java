package com.cmy.service.impl;

import com.cmy.dao.*;
import com.cmy.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/5/5 0005 21:41
 */
@Service(value = "userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException , LockedException {

        boolean flag=true;
        if(null==username || username.length()==0){
            throw new RuntimeException("用户名不能为空");
        }

        //如果不为空则根据用户名获取用户信息
        UserLogin userLogin = userMapper.selectById(username);
        UserLogin userLogin2 = userMapper.selectById(username);
        if(userLogin==null){
            throw new UsernameNotFoundException("这个用户不存在");
        }
        if(userLogin.getActive() == 0 && userLogin.getTime()!=null){
                Date date1=new Date();
            System.out.println(date1);
            long t = date1.getTime();
            t+=30*1000*60;
            Date date = new Date(t);
            System.out.println(date);
            long time = System.currentTimeMillis();
            System.out.println(new Date(time));
            long time2 = userLogin.getTime().getTime();
            if((time-time2)>0){
                userLogin2.setFailsCount(0);
                userLogin2.setTime(null);
                userMapper.updateById(userLogin2);
                flag=true;
            }else{
                flag=false;
            }
        }
        //根据用户获取角色集
        Integer roleId = userInfoMapper.selectById(userLogin.getId()).getRoleId();
        UserRole userRole = userRoleMapper.selectById(roleId);
        List<UserRole> list=new ArrayList<>();
        list.add(userRole);

        return new SecurityUser(userLogin,list,flag);
    }

}
