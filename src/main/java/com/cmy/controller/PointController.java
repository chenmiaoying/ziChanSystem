package com.cmy.controller;

import com.cmy.common.response.CommonResult;
import com.cmy.pojo.Point;
import com.cmy.service.PointService;
import com.cmy.util.APIName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

/**
 * 描述：企业区域站点管理
 *
 * @author 陈淼英
 * @create 2021/4/26 0026 17:38
 */
@RestController
@RequestMapping("/point")
public class PointController {
    @Autowired
    private PointService pointService;

    @APIName("查看区域站点（树形）")
    @GetMapping("/tree")
    public CommonResult getPointTree(){
        return pointService.getPoint();
    }

    @APIName("查看全部区域站点（树形）")
    @GetMapping("/allTree")
    public CommonResult getPointAllTree(){
        return pointService.getPoint();
    }

    @APIName("查看区域站点列表")
    @PostMapping("/list/{limit}/{offset}")
    public CommonResult getPointList(@PathVariable Integer limit, @PathVariable Integer offset, @RequestBody Point p){
        return pointService.getPointTable(limit,offset,p);
    }

    @APIName("添加区域站点")
    @PostMapping("/add")
    public CommonResult addPoint(@RequestBody Point p){
        return pointService.add(p);
    }

    @APIName("删除区域站点")
    @DeleteMapping("/delete/{id}")
    public CommonResult deletePoint(@PathVariable Integer id){
        return pointService.delete(id);
    }

    @APIName("修改区域站点")
    @PostMapping("/update")
    public CommonResult updatePoint(@RequestBody Point point){
        return pointService.update(point);
    }

    @APIName("根据Id获取区域")
    @PostMapping("/{id}")
    public CommonResult getPoint(@PathVariable Integer id){
        return pointService.getPointById(id);
    }
}
