package com.cmy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author
 */

@SpringBootApplication
@MapperScan("com.cmy.dao")
@EnableScheduling
public class CmyApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmyApplication.class, args);
    }

}
