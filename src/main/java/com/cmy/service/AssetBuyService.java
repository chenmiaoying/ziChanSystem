package com.cmy.service;

import com.cmy.common.response.CommonResult;
import com.cmy.pojo.Asset;
import com.cmy.pojo.AssetBuy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/5/26 0026 20:26
 */
public interface AssetBuyService {
    /**
     * 得到采购工单列表数据
     * @param limit
     * @param offset
     * @param assetBuy
     * @return
     */
    CommonResult getBuyList(Integer limit, Integer offset, AssetBuy assetBuy, HttpServletRequest request);

    /**
     * 添加采购工单
     * @param assetBuy
     * @return
     */
    CommonResult addBuy(AssetBuy assetBuy,HttpServletRequest request);

    /**
     * 得到自己发起的申购工单
     * @return
     */
    CommonResult getMyBuyList(Integer limit, Integer offset,HttpServletRequest request);

    /**
     * 登录用户查看需要申购审批
     * @param limit
     * @param offset
     * @param assetBuy
     * @param request
     * @return
     */
    CommonResult getCheckBuyList(Integer limit, Integer offset, AssetBuy assetBuy, HttpServletRequest request);

    /**
     * 申购审批
     * @param assetBuy
     * @return
     */
    CommonResult checkT(Integer flag,AssetBuy assetBuy);
    /**
     * 导出excel
     * @param response 响应
     * @param assetBuy 资产实体类
     */
    void exportList(HttpServletResponse response, AssetBuy assetBuy);

    /**
     * 通过资产ID导出资产
     * @param response 响应
     * @param ids id号
     */
    void exportList(HttpServletResponse response, String ids);

    /**
     * 采购完毕
     */
    CommonResult buyWas(Integer id);
}
