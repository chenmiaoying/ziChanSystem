package com.cmy.common.config;

import com.cmy.filter.CorsFilter;
import com.cmy.filter.JwtFilter;
import com.cmy.filter.LogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.ArrayList;

/**
 * @author Tim
 * @date 2021/4/27
 */
@Configuration
public class FilterConfiguration {
    /**
     * 配置过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(corsFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("corsFilter");
        registration.setOrder(1);
        return registration;
    }
    @Bean
    public FilterRegistrationBean LogFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(logFilter());
        registration.addUrlPatterns("/*");
        registration.setName("logFilter");
        registration.setOrder(5);
        return registration;
    }

    @Bean
    public FilterRegistrationBean jwtFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(jwtFilter());
        registration.addUrlPatterns("/*");
        registration.setName("jwtFilter");
        registration.setOrder(2);
        return registration;
    }

    /**
     * 创建bean
     * @return
     */
    @Bean(name = "corsFilter")
    public Filter corsFilter() {
        return new CorsFilter();
    }
    @Bean(name = "logFilter")
    public Filter logFilter() {
        return new LogFilter();
    }
    @Bean(name = "jwtFilter")
    public Filter jwtFilter() {
        return new JwtFilter();
    }
}
