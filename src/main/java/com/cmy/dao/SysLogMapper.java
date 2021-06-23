package com.cmy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmy.pojo.SysLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Tim
 */
@Mapper
@Repository
public interface SysLogMapper extends BaseMapper<SysLog> {
}
