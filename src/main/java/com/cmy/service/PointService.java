package com.cmy.service;

import com.cmy.common.response.CommonResult;
import com.cmy.pojo.AssetType;
import com.cmy.pojo.Point;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/4/26 0026 17:43
 */
public interface PointService {
    /**
     * 添加区域站点
     * @param p
     * @return
     */
    CommonResult add(Point p);

    /**
     * 修改类型
     * @param p
     * @return
     */
    CommonResult update(Point p);

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
    CommonResult getPoint();
    /**
     * 得到类型(包括软删除的）
     * @return
     */
    CommonResult getAllPoint();

    CommonResult getPointTable(Integer limit ,Integer offset ,Point p);

    CommonResult getPointById(Integer id);

}
