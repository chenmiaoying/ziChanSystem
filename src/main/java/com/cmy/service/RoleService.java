package com.cmy.service;

import com.cmy.common.response.CommonResult;
import com.cmy.dao.UserRoleMapper;
import com.cmy.pojo.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/4/4 0004 17:52
 */
public interface RoleService {
    /**
     * 分页得到所有的用户角色
     * @return
     */
    CommonResult getRoles(Integer limit, Integer offset,UserRole userRole);

    /**
     * 根据Id获取角色权限信息
     * @param userId
     * @return
     */
    CommonResult getMyMenus(String userId);

    /**
     * 得到所有角色
     * @return
     */
    CommonResult getAllRoles();

    /**
     * 根据传送过来的roleId们来进行删除角色
     * @param params
     * @return
     */
    CommonResult deleteRoleById(String params);

    /**
     * 添加角色
     * @param userRole
     * @return
     */
    CommonResult addRole(UserRole userRole);

    CommonResult getRole(String roleId);

    CommonResult updateRole(UserRole userRole);

}
