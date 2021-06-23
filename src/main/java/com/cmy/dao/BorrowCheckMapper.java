package com.cmy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmy.pojo.BorrowCheck;
import com.cmy.pojo.ToDoList;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/4/9 0009 17:05
 */
@Mapper
@Repository
public interface BorrowCheckMapper extends BaseMapper<BorrowCheck> {
    List<ToDoList> getToDoList0(String userId);
    List<ToDoList> getToDoList4(String userId);
    List<ToDoList> getToDoList5(String userId);
}
