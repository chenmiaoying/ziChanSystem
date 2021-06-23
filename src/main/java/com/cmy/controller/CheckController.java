package com.cmy.controller;

import com.cmy.common.response.CommonResult;
import com.cmy.pojo.BorrowCheck;
import com.cmy.service.CheckService;
import com.cmy.util.APIName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/4/9 0009 17:08
 */
@RestController
@RequestMapping("/check")
public class CheckController {

    @Autowired
    CheckService checkService;

    /**
     * 发起借用请求
     * @return
     */
    @APIName("发起借用请求")
    @PostMapping("/borrowCheck")
    public CommonResult addBorrowCheck(@RequestBody BorrowCheck borrowCheck, HttpServletRequest request){
        return checkService.addBCheck(borrowCheck,request);
    }


    /**
     *
     * 借用工单：只展示当前用户发起的借用工单
     * @param limit
     * @param offset
     * @param request
     * @return
     */
    @APIName("查看借用工单")
    @PostMapping("/list/{limit}/{offset}")
    public CommonResult getCheckPages(@PathVariable Integer limit, @PathVariable Integer offset,
                                      @RequestBody BorrowCheck borrowCheck,
                                      HttpServletRequest request){
        return checkService.getChecks(limit,offset,request,borrowCheck);
    }

    @APIName("关闭借用工单")
    @DeleteMapping("/close/{checkId}")
    public CommonResult closeCheck(@PathVariable Integer checkId,HttpServletRequest request){
        return checkService.closeCheck(checkId,request);
    }

    /**
     * 审批
     * @param limit
     * @param offset
     * @param borrowCheck
     * @param request
     * @return
     */
    @APIName("查看借用待审批工单")
    @PostMapping("/checkList/{flag}/{limit}/{offset}")
    public CommonResult getBorrowCheckPages(@PathVariable Integer flag,@PathVariable Integer limit, @PathVariable Integer offset,
                                      @RequestBody BorrowCheck borrowCheck,
                                      HttpServletRequest request){
        return checkService.getBorrowChecks(flag,limit,offset,request,borrowCheck);
    }

    /**
     * 同意工单
     * @param id
     * @return
     */
    @APIName("审批借用工单")
    @PostMapping("/allow/{flag}/{id}")
    public CommonResult allowCheck(@PathVariable Integer flag,@PathVariable Integer id, @RequestBody BorrowCheck borrowCheck,HttpServletRequest request){
        return checkService.allowCheck(flag,id,request,borrowCheck);
    }

    @APIName("资产归还")
    @PostMapping("/back/{id}")
    public CommonResult back(@PathVariable Integer id){
        return checkService.backAsset(id);
    }

    @APIName("资产领用")
    @PostMapping("/use/{id}")
    public CommonResult use(@PathVariable Integer id){
        return checkService.useAsset(id);
    }

    @APIName("借用资产确认归还")
    @PostMapping("/isBack/{id}")
    public CommonResult isBack(@PathVariable Integer id,HttpServletRequest request){
        return checkService.isBackAsset(id,request);
    }

    @APIName("延期借用申请")
    @PostMapping("/moreTime")
    public CommonResult moreTime(@RequestBody BorrowCheck borrowCheck){
        return checkService.moreTime(borrowCheck);
    }

    @APIName("下载领用单")
    @GetMapping("/use/download/{id}")
    public CommonResult downUse(HttpServletResponse response,HttpServletRequest request,@PathVariable Integer id){
        return checkService.downUse(request,response,id);
    }
}
