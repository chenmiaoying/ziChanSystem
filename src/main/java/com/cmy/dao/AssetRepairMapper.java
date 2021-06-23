package com.cmy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmy.pojo.AssetRepair;
import com.cmy.pojo.ToDoList;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/4/23 0023 15:21
 */
@Mapper
@Repository
public interface AssetRepairMapper extends BaseMapper<AssetRepair> {
    List<ToDoList> getToDoList1();
    List<ToDoList> getToDoList3(String userId);
}