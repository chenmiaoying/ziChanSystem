package com.cmy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmy.pojo.AssetType;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/4/17 0017 0:07
 */
@Mapper
@Repository
public interface AssetTypeMapper extends BaseMapper<AssetType> {
    List<AssetType> getType();

    List<AssetType> getAllType();
}
