package com.cmy.controller;

import com.cmy.common.response.CommonResult;
import com.cmy.pojo.AssetType;
import com.cmy.service.AssetTypeService;
import com.cmy.util.APIName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 描述：资产类型管理
 *
 * @author 陈淼英
 * @create 2021/4/16 0016 23:47
 */
@RestController
@RequestMapping("/assetType")
public class AssetTypeController {
    @Autowired
    private AssetTypeService assetTypeService;

    @APIName("查看资产类型数（树状）")
    @GetMapping("/getTypeTree")
    public CommonResult getTypeTree(){
        return assetTypeService.getType();
    }
    @APIName("查看全部资产类型数（树状）")
    @GetMapping("/getAllTypeTree")
    public CommonResult getAllTypeTree(){
        return assetTypeService.getAllTypeTree();
    }

    @APIName("查看资产类型列表")
    @PostMapping("/list/{limit}/{offset}")
    public CommonResult getTypes(@PathVariable Integer limit,@PathVariable Integer offset,@RequestBody AssetType type){
        return assetTypeService.getAllType(limit,offset,type);
    }

    @APIName("添加资产类型")
    @PostMapping("/add")
    public CommonResult add(@RequestBody AssetType assetType){
        return assetTypeService.add(assetType);
    }

    @APIName("更新资产类型")
    @PostMapping("/update")
    public CommonResult update(@RequestBody AssetType assetType){
        return assetTypeService.update(assetType);
    }

    @APIName("根据Id获取资产类型信息")
    @PostMapping("/getType/{id}")
    public CommonResult getType(@PathVariable Integer id){
        return assetTypeService.getTypeById(id);
    }

    @APIName("删除资产类型")
    @DeleteMapping("/{id}")
    public CommonResult delete(@PathVariable Integer id){
        return assetTypeService.delete(id);
    }
}
