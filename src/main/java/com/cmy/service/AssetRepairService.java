package com.cmy.service;

import com.cmy.common.response.CommonResult;
import com.cmy.pojo.Asset;
import com.cmy.pojo.AssetRepair;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/4/23 0023 15:33
 */
public interface AssetRepairService {

    /**
     * 修改资产维护记录
     * @param assetRepair
     * @return
     */
    CommonResult update(AssetRepair assetRepair);

    /**
     * 关闭资产维护工单
     * @param id
     */
    CommonResult close(Integer id);

    /**
     * 资产维护记录类
     * @param limit
     * @param offset
     * @param repair
     * @return
     */
    CommonResult getRepairs(Integer flag,Integer limit ,Integer offset,AssetRepair repair);

    /**
     * 得到自己发出的维修工单
     * @param limit
     * @param offset
     * @param repair
     * @param request
     * @return
     */
    CommonResult getMyRepairs(Integer flag,Integer limit ,Integer offset,AssetRepair repair,HttpServletRequest request);

    /**
     * 新增资产维护单
     * @param repair
     * @return
     */
    CommonResult addRepair(AssetRepair repair, HttpServletRequest request);

    /**
     * 确认工单
     * @param id
     * @param request
     * @return
     */
    CommonResult sureRepair(Integer id,HttpServletRequest request);

    /**
     * 验收工单
     * @param repair
     * @return
     */
    CommonResult acceptRepair(AssetRepair repair);

    /**
     * 维护结束
     * @param id
     * @return
     */
    CommonResult endRepair(Integer id);


}
