package com.cmy.dao;

import com.cmy.pojo.AssetRes;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/4/11 0011 19:18
 */
@Mapper
@Repository
public interface AssetResMapping {
    /**
     * 获取资产名称
     * @return
     */
    List<AssetRes> getName();
}
