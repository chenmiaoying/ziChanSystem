package com.cmy.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cmy.common.response.CommonResult;
import com.cmy.dao.MenuMapper;
import com.cmy.dao.RoleMenuMapper;
import com.cmy.dao.UserInfoMapper;
import com.cmy.dao.UserRoleMapper;
import com.cmy.pojo.Asset;
import com.cmy.pojo.Menu;
import com.cmy.pojo.RoleMenu;
import com.cmy.pojo.UserRole;
import com.cmy.service.MenuService;
import com.cmy.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/3/31 0031 21:36
 */

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> selectMenusByRoleId(Integer roleId) {

        //存储菜单列表
        List<Menu> menus=new ArrayList<>();
        //暂存一级菜单
        Menu m1=null;
        //暂存二级菜单
        Menu m2=null;
        Menu m3=null;
        //获取全部的一级菜单
        List<Menu> parentMenus=menuMapper.selectParentByRoleId(roleId);
        //循环一级菜单
        for (Menu menu1:parentMenus) {
            m1=cloneMenu(menu1);
            //获取当前一级菜单的所有二级菜单
            List<Menu> childrenMenus=menuMapper.selectChild(roleId,menu1.getMenuId());
            if(!childrenMenus.isEmpty()){
                //循环匹配二级菜单
                for (Menu menu2:childrenMenus) {
                    m2=cloneMenu(menu2);
                    List<Menu> childrenChildMenus=menuMapper.selectChildChild(roleId,menu2.getMenuId());
                    if(!childrenChildMenus.isEmpty()) {
                        for (Menu menu3:childrenChildMenus){
                            //将功能管理加入二级菜单
                            m3=cloneMenu(menu3);
                            m2.getChildren().add(m3);
                        }
                        //将二级菜单加入一级菜单
                        m1.getChildren().add(m2);
                    }
                }
                if(!m1.getChildren().isEmpty()){
                    menus.add(m1);
                }
            }

        }
        return menus;
    }

    @Override
    public CommonResult getMenus(Integer roleId) {
        return CommonResult.successCommonResult(selectMenusByRoleId(roleId),"菜单查询成功");
    }

    @Override
    public CommonResult getAllMenus() {
        return CommonResult.successCommonResult(menuMapper.getAllMenus(),"查询成功");
    }

    /**
     * 存储菜单列表
     * @param src
     * @return
     */
    private Menu cloneMenu(Menu src) {
        Menu menu = new Menu();
        menu.setMenuId(src.getMenuId());
        menu.setMenuIcon(src.getMenuIcon());
        menu.setMenuLabel(src.getMenuLabel());
        menu.setMenuUrl(src.getMenuUrl());
        menu.setParentId(src.getParentId());
        menu.setChildren(new ArrayList<Menu>());
        return menu;
    }

    /**
     * 判断一个整数是否在一个数组里面
     * @param arr
     * @return
     */
    public boolean isIn(String a,Integer[] arr){
        Integer a2=Integer.parseInt(a);
        for (Integer i:arr) {
            if(a2==i) {
                return true;
            }
        }
        return false;
    }
    public boolean isIn(Integer a,String[] arr){
        for (String i:arr) {
            Integer i1=Integer.parseInt(i);
            if(a==i1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public CommonResult editRoleMenu(Integer roleId, String menuIdStr) {
        String[] split = menuIdStr.split(",");
        Integer[] menuId = roleMenuMapper.getMenuId(roleId);
        Integer[] del=new Integer[menuId.length];
        Integer[] add=new Integer[split.length];
        int index=0;
        for (Integer i: menuId) {
            boolean in = isIn(i, split);
            if(!in){
                del[index]=i;
                index++;
            }
        }
        index=0;
        for (String i: split) {
            boolean in = isIn(i, menuId);
            if(!in){
                add[index]=Integer.parseInt(i);
                index++;
            }
        }
        if(del.length !=0){
            roleMenuMapper.delete(new QueryWrapper<RoleMenu>().in("menuId",del).eq("role_id",roleId));
        }
        if(add[0] != null){
            for (Integer i:add) {
                if(i!=null){
                    RoleMenu roleMenu=new RoleMenu();
                    roleMenu.setRoleId(roleId);
                    roleMenu.setMenuId(i);
                    roleMenuMapper.insert(roleMenu);
                }
            }
        }
        return CommonResult.successCommonResult("修改成功");
    }
    @Override
    public CommonResult deleteRightById(Integer id,Integer roleId) {
        //id是权限id
        int delete = roleMenuMapper.delete(new QueryWrapper<RoleMenu>().eq("menuId", id).eq("role_id", roleId));
        if(delete!=0){
            return CommonResult.successCommonResult("删除成功");
        }
        return CommonResult.errorCommonResult("删除失败");
    }
}
