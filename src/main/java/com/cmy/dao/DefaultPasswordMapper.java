package com.cmy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmy.pojo.DefaultPassword;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/6/4 0004 0:50
 */
@Mapper
@Repository
public interface DefaultPasswordMapper extends BaseMapper<DefaultPassword> {
}
