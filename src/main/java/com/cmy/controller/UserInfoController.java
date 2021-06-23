package com.cmy.controller;

import com.cmy.common.response.CommonResult;
import com.cmy.pojo.UserInfo;
import com.cmy.service.RoleService;
import com.cmy.service.UserInfoService;
import com.cmy.util.APIName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private RoleService roleService;

    @APIName("查看人员管理列表")
    @PostMapping("/list/{limit}/{offset}")
    public CommonResult selectUserInfo(@PathVariable Integer limit,@PathVariable Integer offset, @RequestBody UserInfo userInfo){
        return userInfoService.selectUserInfo(limit,offset,userInfo);
    }
    @APIName("删除用户")
    @DeleteMapping("/delete/{limit}/{offset}/{id}")
    public CommonResult deleteById(@PathVariable Integer limit,@PathVariable Integer offset,@PathVariable Integer id){
        return userInfoService.deleteById(limit,offset,id);
    }

    @APIName("根据Id获取用户信息")
    @GetMapping("/list/{id}")
    public CommonResult getUserById(@PathVariable Integer id){
        return userInfoService.getUserById(id);
    }

    @APIName("添加用户")
    @PostMapping("/add")
    public CommonResult addUser(@RequestBody UserInfo userInfo){
        return userInfoService.addUser(userInfo);
    }

    @APIName("个人资料卡")
    @PostMapping("/peopleDetail")
    public CommonResult showDetail(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        return userInfoService.showById(token);
    }

    @APIName("用户信息修改")
    @PostMapping("/update")
    public CommonResult update(@RequestBody UserInfo userInfo){
        return userInfoService.update(userInfo);
    }

    @APIName("个人信息修改")
    @PostMapping("/updateMy")
    public CommonResult updateMy(@RequestBody UserInfo userInfo){
        return userInfoService.update(userInfo);
    }

    /**
     * 重置密码
     */
    @APIName("重置密码")
    @PostMapping("/reset/{id}")
    public CommonResult resetUser(@PathVariable String id,HttpServletRequest request){
        return userInfoService.userReset(id);
    }


}
