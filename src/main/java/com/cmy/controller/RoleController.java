package com.cmy.controller;

import com.cmy.common.response.CommonResult;
import com.cmy.pojo.UserRole;
import com.cmy.service.RoleService;
import com.cmy.service.UserInfoService;
import com.cmy.util.APIName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/4/9 0009 14:11
 */
@RestController
@RequestMapping("/userRole")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @APIName("添加角色")
    @PostMapping("/addRole")
    public CommonResult addRole(@RequestBody UserRole userRole){
        return roleService.addRole(userRole);
    }

    @APIName("根据Id获取角色")
    @PostMapping("/getRole/{id}")
    public CommonResult getRole(@PathVariable String id){
        return roleService.getRole(id);
    }

    @APIName("修改角色描述")
    @PostMapping("/updateRole")
    public CommonResult updateRole(@RequestBody UserRole userRole){
        return roleService.updateRole(userRole);
    }

    //得到所有角色
    @APIName("获得全部角色")
    @GetMapping("/getAllRoles")
    public CommonResult getAllRoles(){
        return roleService.getAllRoles();
    }

    //分页得到所有的角色
    @APIName("查看角色信息列表")
    @PostMapping("/getRoles/{limit}/{offset}")
    public CommonResult getAllRole(@PathVariable Integer limit,@PathVariable Integer offset,@RequestBody UserRole userRole){
        return roleService.getRoles(limit,offset,userRole);
    }

    @APIName("删除角色")
    @DeleteMapping("/deleteRole")
    public CommonResult deleteRole(@RequestParam(name = "param") String param){
        return roleService.deleteRoleById(param);
    }

}
