package com.cmy.controller;

import com.cmy.common.response.CommonResult;
import com.cmy.pojo.AssetRepair;
import com.cmy.service.AssetRepairService;
import com.cmy.util.APIName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/4/23 0023 15:43
 */
@RestController
@RequestMapping("/repair")
public class AssetRepairController {

    @Autowired
    private AssetRepairService assetRepairService;

    @APIName("查看资产维护工单（维修员）")
    @PostMapping("/list/{flag}/{limit}/{offset}")
    public CommonResult getRepair(@PathVariable Integer flag,@PathVariable Integer limit, @PathVariable Integer offset,
                                  @RequestBody AssetRepair assetRepair){
        return assetRepairService.getRepairs(flag,limit,offset,assetRepair);
    }
    @APIName("查看发起维修工单")
    @PostMapping("/myList/{flag}/{limit}/{offset}")
    public CommonResult getMyRepair(@PathVariable Integer flag,@PathVariable Integer limit, @PathVariable Integer offset,
                                  @RequestBody AssetRepair assetRepair,
                                  HttpServletRequest request){
        return assetRepairService.getMyRepairs(flag,limit,offset,assetRepair,request);
    }
    @APIName("验收工单")
    @PostMapping("/accept")
    public CommonResult acceptRepair(@RequestBody AssetRepair repair){
        return assetRepairService.acceptRepair(repair);
    }
    @APIName("发起维修工单")
    @PostMapping("/add")
    public CommonResult addRepair(@RequestBody AssetRepair assetRepair, HttpServletRequest request){
        return assetRepairService.addRepair(assetRepair,request);
    }

    @APIName("关闭维修工单")
    @DeleteMapping("/close/{id}")
    public CommonResult closeRepair(@PathVariable Integer id){
        return assetRepairService.close(id);
    }
    /**
     * 确认维修工单
     * @param id
     * @param request
     * @return
     */
    @APIName("维修员确认维修工单")
    @PostMapping("/sure/{id}")
    public CommonResult sureRepair(@PathVariable Integer id, HttpServletRequest request){
        return assetRepairService.sureRepair(id,request);
    }
    @APIName("确认维修结束")
    @PostMapping("/end/{id}")
    public CommonResult endRepair(@PathVariable Integer id){
        return assetRepairService.endRepair(id);
    }
}
