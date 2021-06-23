package com.cmy.service;

import com.cmy.common.response.CommonResult;
import com.cmy.pojo.Asset;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

public interface AssetQueryService {
    /**
     * 获取全部资产信息
     * @return
     */
    CommonResult<Asset> getAssetAll();

    /**
     * 获取所有资产名称
     * @return
     */
    CommonResult getAssetName();
    /**
     * 通过 用户输入条件来查询资产，获得对应的资产信息
     * @param limit 第几页
     * @param offset 偏移量-每页多少条记录
     * @param asset 用户输入查询条件
     * @return
     */
    CommonResult getAsset(Integer limit , Integer offset , Asset asset);

    /**
     * 导出excel
     * @param response 响应
     * @param asset 资产实体类
     */
    void exportList(HttpServletResponse response, Asset asset,Integer del);

    /**
     * 通过资产ID导出资产
     * @param response 响应
     * @param assetIds id号
     */
    void exportList(HttpServletResponse response, String assetIds,Integer del);

    /**
     * 根据资产ID删除资产
     * @param deleteId 删除资产所在Id
     * @param token 删除操作者token
     * @param reason 删除资产原因
     */
    CommonResult deleteAssetById(Integer deleteId,String token,String reason);

    /**
     * 通过资产ID查询资产信息
     * @param assetId 资产ID
     * @return
     */
    Asset getAssetById(Integer assetId);

    /**
     * 修改资产信息
     * @param asset 资产信息
     */
    void updateAsset(Asset asset);

    /**
     * 添加资产信息
     * @param asset 资产实体类
     */
    void addAsset(Asset asset,String ids);

    /**
     * excel模板下载
     * @param response 响应
     */
    void downloadExcelTemplate(HttpServletResponse response);

    /**
     * 通过excel批量上传
     * @param inputStream 输入流
     * @return
     */
    CommonResult excelsImport(InputStream inputStream);

}
