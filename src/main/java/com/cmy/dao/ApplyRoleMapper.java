package com.cmy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmy.pojo.ApplyRole;
import com.cmy.pojo.ToDoList;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/5/31 0031 13:41
 */
@Mapper
@Repository
public interface ApplyRoleMapper extends BaseMapper<ApplyRole> {
    List<ToDoList> getToDoList0();
}
