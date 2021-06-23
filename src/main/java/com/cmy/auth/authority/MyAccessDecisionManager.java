package com.cmy.auth.authority;

import com.alibaba.fastjson.JSON;
import com.cmy.dao.UserInfoMapper;
import com.cmy.dao.UserMapper;
import com.cmy.pojo.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/5/6 0006 15:54
 */
@Component("myAccessDecisionManager")
public class MyAccessDecisionManager implements AccessDecisionManager {

    /**
     * 判断当前登录的用户是否具备当前请求URL所需要的角色信息
     * 如果不具备，就抛出 AccessDeniedException 异常，否则不做任何事即可
     *
     * @param authentication 当前登录用户的信息
     * @param o         FilterInvocation对象，可以获取当前请求对象
     * @param collection     FilterInvocationSecurityMetadataSource 中的 getAttributes() 方法的返回值，
     *                       即当前请求URL所需的角色

     */
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection)
            throws AccessDeniedException, InsufficientAuthenticationException {

        Collection<? extends GrantedAuthority> auths = authentication.getAuthorities();
        for (ConfigAttribute configAttribute : collection) {
			/*
			  如果需要的角色是null，说明当前请求的URL用户登陆后即可访问；
			  如果authentication是UsernamePasswordAuthenticationToken实例，说明当前用户已登录。
			 */
            if(configAttribute.getAttribute().equals("disable")){
                throw new AccessDeniedException("您没有权限进行此操作");
            }
            if ( collection == null) {
                return;
            }
            for (GrantedAuthority authority : auths) {
                if (configAttribute.getAttribute().equals(authority.getAuthority())) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("权限不足！");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
