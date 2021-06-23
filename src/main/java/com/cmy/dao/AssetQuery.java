package com.cmy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmy.pojo.Asset;
import com.cmy.pojo.AssetRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
/**
 * @author 陈淼英
 * @create 2021/2/13 0004 22:48
 */

@Mapper
@Repository
public interface AssetQuery extends BaseMapper<Asset> {
    /**
     *  获取全部资产信息
     * @return
     */
    List<Asset> queryAll();
    /**
     * 通过 用户输入条件来查询资产，获得对应的资产信息
     * @param params 用户输入查询条件
     * @return
     */
    List<Asset> getQueryBy(@Param("params") Map<String, String> params);
}
