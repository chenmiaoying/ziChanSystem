package com.cmy.pojo;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/5/7 0007 11:37
 */
public class SecurityUser implements UserDetails {
    /**
     * 当前登录用户
     */
    private transient UserLogin currentUserInfo;
    private transient boolean locked;
    /**
     * 角色
     */
    private transient List<UserRole> roleList;

    public SecurityUser() { }

    public SecurityUser(UserLogin user, boolean b) {
        if (user != null) {
            this.currentUserInfo = user;
            this.locked=b;
        }
    }

    public SecurityUser(UserLogin user, List<UserRole> roleList,boolean b) {
        if (user != null) {
            this.currentUserInfo = user;
            this.roleList = roleList;
            this.locked=b;
        }
    }

    /**
     * 获取当前用户所具有的角色
     *
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (!CollectionUtils.isEmpty(this.roleList)) {
            for (UserRole role : this.roleList) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getCode());
                authorities.add(authority);
            }
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return currentUserInfo.getPassword();
    }

    @Override
    public String getUsername() {
        return currentUserInfo.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
