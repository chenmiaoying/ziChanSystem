package com.cmy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmy.pojo.UserLogin;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author 陈淼英
 * @create 2021/2/14 0004 22:48
 */
@Mapper
@Repository
public interface UserMapper extends BaseMapper<UserLogin> {
        /**
         * 通过用户名获取用户详情
         * @param username 用户名
         * @return
         */
        UserLogin getUserInfoByName(String username);
}
