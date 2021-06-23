package com.cmy;

import com.cmy.dao.UserInfoMapper;
import com.cmy.dao.UserMapper;
import com.cmy.pojo.UserLogin;

import com.cmy.service.AssetQueryService;

import com.cmy.util.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootTest
class CmyApplicationTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void test(){
        System.out.println(passwordEncoder.encode("admin"));
    }


}
