package com.cmy.auth;

import com.cmy.dao.UserInfoMapper;
import com.cmy.dao.UserMapper;
import com.cmy.dao.UserRoleMapper;
import com.cmy.pojo.UserLogin;
import com.cmy.pojo.UserRole;
import com.cmy.util.AesUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/5/6 0006 10:35
 */
public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        if (request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE)
                || request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            if (!request.getMethod().equals("POST")) {
                throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
            }
            Map<String,String> map=new HashMap<>();
            /**
             * Jackson ObjectMapper可以从字符串、流或文件解析JSON，并创建Java对象或对象图来表示已解析的JSON
             */
            ObjectMapper mapper = new ObjectMapper();
            try (InputStream is = request.getInputStream()) {
                map = mapper.readValue(is, Map.class);//json转为map对象
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(map!=null){
                String username=map.get("id");

                String password=map.get("password");
                username = username != null ? username : "";
                username = username.trim();
                password = password != null ? password : "";

                String jsData=null;
                try {

                    jsData = AesUtil.aesDecrypt(password, "cBssbHB3ZA==HKXT");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, jsData);
                this.setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            }
            return null;
        }
        return null;
    }
}
