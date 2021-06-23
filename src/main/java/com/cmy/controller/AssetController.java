package com.cmy.controller;

import com.alibaba.fastjson.JSON;
import com.cmy.common.response.CommonResult;
import com.cmy.pojo.Asset;
import com.cmy.service.AssetQueryService;
import com.cmy.util.APIName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/asset")
public class AssetController {

    @Autowired
    AssetQueryService assetQueryService;

    @APIName("查询全部资产信息")
    @PostMapping("/{limit}/{offset}")
    public CommonResult getAsset(@PathVariable Integer limit,
                                 @PathVariable Integer offset,
                                 @RequestBody Asset asset){
        return assetQueryService.getAsset(limit,offset,asset);
    }
    @APIName("查询全部资产信息")
    @PostMapping("/getAssetName")
    public CommonResult getAsset(){
        return assetQueryService.getAssetName();
    }

    //资产导出
    @APIName("条件导出资产信息")
    @GetMapping("/list/export/{del}/{param}")
    public void exportList(HttpServletResponse response,@PathVariable String param,@PathVariable Integer del){
        Asset asset = JSON.parseObject(param, Asset.class);
        assetQueryService.exportList(response,asset,del);
    }
    //复选框选择导出
    @APIName("选择导出资产信息")
    @GetMapping("/list/exportByIds/{del}/{param}")
    public void exportListByIds(HttpServletResponse response,@PathVariable String param,@PathVariable Integer del){
        assetQueryService.exportList(response,param,del);
    }

    @APIName("删除资产")
    @DeleteMapping("/{deleteId}/{reason}")
    public CommonResult deleteAssetById(HttpServletRequest request, @PathVariable Integer deleteId, @PathVariable String reason){
        String token = request.getHeader("Authorization");
        assetQueryService.deleteAssetById(deleteId,token,reason);
        return assetQueryService.deleteAssetById(deleteId,token,reason);
    }

    @APIName("根据ID查询资产信息")
    @GetMapping("/{assetId}")
    public CommonResult getAssetById(@PathVariable Integer assetId){
        return CommonResult.successCommonResult(assetQueryService.getAssetById(assetId),"查询成功");
    }

    @APIName("修改资产信息")
    @PostMapping("/update")
    public CommonResult updateAsset(@RequestBody Asset asset){
        assetQueryService.updateAsset(asset);
        return CommonResult.successCommonResult("修改成功");
    }



    @APIName("添加单台资产信息")
    @PostMapping("/add/{param}")
    public CommonResult addAsset(@PathVariable String param,@RequestBody Asset asset){
        assetQueryService.addAsset(asset,param);
        return CommonResult.successCommonResult("添加成功");
    }

    @APIName("导出批量资产入库模板")
    @GetMapping("/excel/template")
    public void downloadExcelTemplate(HttpServletResponse response){
        assetQueryService.downloadExcelTemplate(response);
    }

    @APIName("批量资产入库")
    @PostMapping("/excel/upload")
    public CommonResult excelsImport(@RequestParam("file") MultipartFile multipartFile) {
        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
        } catch (IOException e) {
            return CommonResult.errorCommonResult("文件上传失败");
        }
        return assetQueryService.excelsImport(inputStream);
    }
}

