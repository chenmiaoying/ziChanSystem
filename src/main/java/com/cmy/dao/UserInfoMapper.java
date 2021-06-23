package com.cmy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmy.pojo.ToDoList;
import com.cmy.pojo.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    List<ToDoList> getToDoList0();
}
