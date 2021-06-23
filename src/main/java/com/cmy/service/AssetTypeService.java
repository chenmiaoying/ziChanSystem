package com.cmy.service;

import com.cmy.common.response.CommonResult;
import com.cmy.pojo.AssetType;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/4/17 0017 0:00
 */
public interface AssetTypeService {
    /**
     * 添加资产类型
     * @param t
     * @return
     */
    CommonResult add(AssetType t);

    /**
     * 修改类型
     * @param t
     * @return
     */
    CommonResult update(AssetType t);

    /**
     * 根据id删除类型
     * @param id
     * @return
     */
    CommonResult delete(Integer id);

    /**
     * 得到类型
     * @return
     */
    CommonResult getType();
    CommonResult getAllTypeTree();
    CommonResult getAllType(Integer limit ,Integer offset ,AssetType type);

    CommonResult getTypeById(Integer id);

}
