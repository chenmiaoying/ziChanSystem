package com.cmy.auth;

import com.cmy.dao.UserMapper;
import com.cmy.pojo.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import java.util.Date;
/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/6/3 0003 0:19
 */

/**
 * 登陆失败监听
 *
 */
@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent authenticationFailureBadCredentialsEvent) {
        String userId = authenticationFailureBadCredentialsEvent.getAuthentication().getPrincipal().toString();
        UserLogin user = userMapper.selectById(userId);
        if (user != null) {
            // 用户失败次数
            Integer fails = user.getFailsCount();
            Date time1 = user.getTime();
            if(fails==null){
                fails = 0;
            }
            if(time1!=null){
                Date date1=new Date();//现在的时间
                int result=date1.compareTo(time1);
                if(result>0){
                    fails=0;
                    user.setFailsCount(fails);
                    user.setTime(null);
                    userMapper.updateById(user);
                }
            }
            fails++;
            if(fails>=5){

                Long time=System.currentTimeMillis();
                time += 30*1000*60 ;
                Date date = new Date(time);

                user.setActive(0);
                user.setTime(date);
                user.setFailsCount(fails);
                userMapper.updateById(user);
            } else{
                user.setFailsCount(fails);
                userMapper.updateById(user);
            }
        }
    }
}
