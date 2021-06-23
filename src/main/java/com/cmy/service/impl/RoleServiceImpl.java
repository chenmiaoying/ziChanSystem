package com.cmy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmy.common.response.CommonResult;
import com.cmy.dao.UserInfoMapper;
import com.cmy.dao.UserRoleMapper;
import com.cmy.pojo.Menu;
import com.cmy.pojo.UserInfo;
import com.cmy.pojo.UserRole;
import com.cmy.service.MenuService;
import com.cmy.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：角色管理
 *
 * @author 陈淼英
 * @create 2021/4/4 0004 17:54
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private MenuService menuService;

    @Override
    public CommonResult getRoles(Integer limit, Integer offset,UserRole userRole) {
        Page<UserRole> userRolePage = userRoleMapper.selectPage(new Page<>(limit, offset), new QueryWrapper<>(userRole));
        List<UserRole> records = userRolePage.getRecords();
        //存储用户列表
        List<UserRole> records2=new ArrayList<>();
        for (UserRole u:records) {
            u.setChildren(menuService.selectMenusByRoleId(u.getRoleId()));
            records2.add(u);
        }
        userRolePage.setRecords(records2);
        return CommonResult.successCommonResult(userRolePage,"查询成功");

    }
    @Override
    public CommonResult getMyMenus(String userId) {
        Integer roleId = userInfoMapper.selectById(userId).getRoleId();
        UserRole userRole = userRoleMapper.selectById(roleId);
        userRole.setChildren(menuService.selectMenusByRoleId(roleId));
        return CommonResult.successCommonResult(userRole,"查询成功");
    }

    @Override
    public CommonResult getAllRoles() {
        List<UserRole> userRoles = userRoleMapper.getRoles();
        return  CommonResult.successCommonResult(userRoles,"查询成功");
    }

    @Override
    public CommonResult deleteRoleById(String params) {
        //设置不可以删除超级管理员
        String[] roleIds = params.split(",");

        List<Integer> newIds=new ArrayList<>();
        Boolean flag=true;
        String str="全部删除成功";
        for (String i:roleIds) {

            if(i.equals("1")){
                flag=false;
            }
            if(!i.equals("1")) {
                newIds.add(Integer.parseInt(i));
            }
        }
        if(!flag){
            str="不可以删除超级管理员,其余已删除";
        }
        userRoleMapper.delete(new QueryWrapper<UserRole>().in("role_id",newIds));


        return CommonResult.successCommonResult(str);
    }

    @Override
    public CommonResult addRole(UserRole userRole) {
        Integer roleIdByRole = userRoleMapper.getRoleIdByRole(userRole.getUserRole());
        if(roleIdByRole!=null){
            return CommonResult.errorCommonResult("该角色已经存在，请换一个角色名");
        }
        if(userRole.getUserRole()!=null){
            userRole.setCode("ROLE_"+userRole.getUserRole());
            userRoleMapper.insert(userRole);
            return CommonResult.successCommonResult("添加成功，请分配该角色的权限");
        }else{
            return CommonResult.errorCommonResult("角色名不能为空");
        }
    }

    @Override
    public CommonResult getRole(String roleId) {
        return CommonResult.successCommonResult(userRoleMapper.selectById(roleId),"查询成功");
    }

    @Override
    public CommonResult updateRole(UserRole userRole) {
        int i = userRoleMapper.updateById(userRole);
        if(i==1){
            return CommonResult.successCommonResult("修改成功");
        }
        return CommonResult.successCommonResult("修改失败");
    }
}
