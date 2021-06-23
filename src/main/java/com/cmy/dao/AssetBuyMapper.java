package com.cmy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmy.pojo.AssetBuy;
import com.cmy.pojo.ToDoList;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：申购工单
 *
 * @author 陈淼英
 * @create 2021/5/26 0026 14:15
 */
@Mapper
@Repository
public interface AssetBuyMapper extends BaseMapper<AssetBuy> {
    List<ToDoList> getToDoList0(String userId);
}
