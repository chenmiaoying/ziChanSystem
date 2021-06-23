package com.cmy.auth.authority;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cmy.dao.MenuMapper;
import com.cmy.dao.RoleMenuMapper;
import com.cmy.dao.UserRoleMapper;
import com.cmy.pojo.Menu;
import com.cmy.pojo.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/5/6 0006 15:40
 */
@Component("myFilterInvocationSecurityMetadataSource")
public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    /**
     * 接口角色列表
     */
    private static List<Menu> menuList;

    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private MenuMapper menuMapper;

    @PostConstruct
    private void loadDataSource() {
        menuList = menuMapper.selectList(null);
    }

    /**
     * 清空接口角色信息
     */
    public void clearDataSource() {
        menuList = null;
    }

    /**
     * 返回被拦截路径需要的权限信息
     * @param object
     * @return
     * @throws IllegalArgumentException
     */



    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        // 修改接口角色关系后重新加载
        if (CollectionUtils.isEmpty(menuList)) {
            this.loadDataSource();
        }
        HttpServletRequest request = ((FilterInvocation)object).getRequest();
        // 获取用户请求方式
        String method = request.getMethod();
        // 获取用户请求Url
        String url = request.getRequestURI();
        AntPathMatcher antPathMatcher = new AntPathMatcher();

        if(url.equals("/getVerifyCode")||url.equals("/register")){
            return null;
        }

        // 获取接口角色信息，若为匿名接口则放行，若无对应角色则禁止
        for (Menu menu : menuList) {

            if (menu.getMenuUrl()!=null && antPathMatcher.match(menu.getMenuUrl(), url) && menu.getMethodType()!=null
                    && menu.getMethodType().equals(method)) {

                Integer menuId = menu.getMenuId();//说明该访问是受保护的

                List<String> roleCodeList = roleMenuMapper.getRoleCodeList(menuId);
                if (CollectionUtils.isEmpty(roleCodeList)) {
                    //受保护的但是却没有任何角色匹配，说明该访问任何角色都不可以访问
                    return SecurityConfig.createList("disable");
                }
                return SecurityConfig.createList(roleCodeList.toArray(new String[]{}));
            }
        }
//        // 如果数据中没有找到相应url资源则为非法访问，要求用户登录再进行操作
//        return SecurityConfig.createList("ROLE_LOGIN");
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
