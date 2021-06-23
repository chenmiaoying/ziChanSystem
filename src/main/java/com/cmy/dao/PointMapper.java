package com.cmy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmy.pojo.Point;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/4/26 0026 17:00
 */
@Mapper
@Repository
public interface PointMapper extends BaseMapper<Point> {
    List<Point> getPoint();

    List<Point> getAllPoint();
}
