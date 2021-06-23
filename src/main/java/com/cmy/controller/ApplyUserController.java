package com.cmy.controller;

import com.cmy.common.response.CommonResult;
import com.cmy.dao.ApplyRoleMapper;
import com.cmy.dao.UserInfoMapper;
import com.cmy.pojo.ApplyRole;
import com.cmy.pojo.UserInfo;
import com.cmy.pojo.UserLogin;
import com.cmy.pojo.UserVo;
import com.cmy.service.MenuService;
import com.cmy.service.RoleService;
import com.cmy.service.UserInfoService;
import com.cmy.util.APIName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 描述：用户申请
 *
 * @author 陈淼英
 * @create 2021/4/31 0031 9:18
 */
@RestController
@RequestMapping("/apply")
public class ApplyUserController {
    @Autowired
    private UserInfoService userInfoService;


    @Autowired
    private RoleService roleService;


    /**
     * 账号申请获取
     * @param limit
     * @param offset
     * @param userVo
     * @return
     */
    @APIName("查看账号申请信息")
    @PostMapping("/account/{limit}/{offset}")
    public CommonResult getApplyAccount(@PathVariable Integer limit,
                                        @PathVariable Integer offset, @RequestBody UserLogin userVo){
        return userInfoService.getAccount(limit,offset,userVo);
    }
    /**
     * 权限申请获取
     */
    @APIName("查看权限申请信息")
    @PostMapping("/role/{limit}/{offset}")
    public CommonResult getApplyRole(@PathVariable Integer limit,
                                        @PathVariable Integer offset, @RequestBody UserLogin userVo){
        return userInfoService.getApplyRole(limit,offset,userVo);
    }

    /**
     * 账号审批
     * @param flag
     * @param userInfo
     * @return
     */
    @APIName("审批申请账号")
    @PostMapping("/sureAccount/{flag}")
    public CommonResult sureAccount(@PathVariable Integer flag,@RequestBody UserInfo userInfo){
        return userInfoService.sureAccount(flag,userInfo);
    }

    /**
     * 审批权限申请
     * @param flag
     * @param idStr
     * @return
     */
    @APIName("审批权限申请")
    @PostMapping("/sureRole/{flag}/{id}")
    public CommonResult sureRole(@PathVariable Integer flag,@PathVariable Integer id,@RequestParam("idStr") String idStr){

        return userInfoService.sureApplyRole(flag,id,idStr);
    }

    /**
     * 申请权限
     * @param applyRole
     * @return
     */
    @APIName("个人申请权限")
    @PostMapping("/role")
    public CommonResult applyRole(@RequestBody ApplyRole applyRole){
        return userInfoService.applyRole(applyRole);
    }


    @APIName("获得个人全部权限")
    @PostMapping("/myMenu/{userId1}")
    public CommonResult getOneMenu(@PathVariable String userId1){
        roleService.getMyMenus(userId1);
        return roleService.getMyMenus(userId1);
    }
}
