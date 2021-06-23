package com.cmy.controller;

import com.alibaba.fastjson.JSON;
import com.cmy.common.response.CommonResult;
import com.cmy.pojo.Asset;
import com.cmy.pojo.AssetBuy;
import com.cmy.service.AssetBuyService;
import com.cmy.util.APIName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/5/26 0026 14:19
 */
@RestController
@RequestMapping("/assetBuy")
public class AssetBuyController {
    @Autowired
    private AssetBuyService assetBuyService;


    @APIName("查看采购工单")
    @PostMapping("/buyList/{limit}/{offset}")
    public CommonResult getBuyList(@PathVariable Integer limit,
                                   @PathVariable Integer offset, @RequestBody AssetBuy assetBuy, HttpServletRequest request){
        return assetBuyService.getBuyList(limit,offset,assetBuy,request);
    }

    @APIName("发起资产申购")
    @PostMapping("/add")
    public CommonResult addBuy(@RequestBody AssetBuy assetBuy,HttpServletRequest request){
        return assetBuyService.addBuy(assetBuy,request);
    }

    @APIName("查看自己的申购工单")
    @GetMapping("/MyBuyList/{limit}/{offset}")
    public CommonResult getMyBuyList(@PathVariable Integer limit,
                                   @PathVariable Integer offset,HttpServletRequest request){
        return assetBuyService.getMyBuyList(limit,offset,request);
    }
    @APIName("审核员接收申购工单")
    @PostMapping("/checkList/{limit}/{offset}")
    public CommonResult getCheckBuyList(@PathVariable Integer limit,
                                   @PathVariable Integer offset, @RequestBody AssetBuy assetBuy, HttpServletRequest request){
        return assetBuyService.getCheckBuyList(limit,offset,assetBuy,request);
    }

    @APIName("申购审批")
    @PostMapping("/check/{flag}")
    public CommonResult checkT(@PathVariable Integer flag,@RequestBody AssetBuy assetBuy){
        return assetBuyService.checkT(flag,assetBuy);
    }

    //资产导出
    @APIName("条件导出采购工单")
    @GetMapping("/list/export/{param}")
    public void exportList(HttpServletResponse response, @PathVariable String param){
        AssetBuy assetBuy = JSON.parseObject(param, AssetBuy.class);
        assetBuyService.exportList(response,assetBuy);
    }
    //复选框选择导出
    @APIName("选择导出采购工单")
    @GetMapping("/list/exportByIds")
    public void exportListByIds(HttpServletResponse response,@RequestParam(name = "ids") String param){
        assetBuyService.exportList(response,param);
    }

    @APIName("确认采购完毕")
    @PostMapping("/buyWas/{id}")
    private CommonResult buyWas(@PathVariable Integer id){
        return assetBuyService.buyWas(id);
    }
}
