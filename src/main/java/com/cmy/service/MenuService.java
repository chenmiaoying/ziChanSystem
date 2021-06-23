package com.cmy.service;

import com.cmy.common.response.CommonResult;
import com.cmy.pojo.Menu;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 描述：权限菜单
 * @author 陈淼英
 * @create 2021/3/31 0031 15:09
 */
public interface MenuService {

    /**
     * 根据roleId
     * @param roleId
     * @return
     */
    List<Menu> selectMenusByRoleId(Integer roleId);

    /**
     * 得到菜单
     * @param roleId
     * @return
     */
    CommonResult getMenus(Integer roleId);

    /**
     * 得到全部菜单
     * @return
     */
    CommonResult getAllMenus();

    CommonResult editRoleMenu(Integer roleId,String menuIdStr);

    /**
     * 根据menuId删除权限
     * @param id
     * @return
     */
    CommonResult deleteRightById(Integer id,Integer roleId);

}
