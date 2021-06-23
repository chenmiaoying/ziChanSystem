package com.cmy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmy.pojo.DoList;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/4/29 0029 18:35
 */
@Mapper
@Repository
public interface DoListMapper extends BaseMapper<DoList> {
}
