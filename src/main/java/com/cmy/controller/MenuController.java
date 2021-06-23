package com.cmy.controller;

import com.cmy.common.response.CommonResult;
import com.cmy.dao.UserInfoMapper;
import com.cmy.dao.UserMapper;
import com.cmy.pojo.UserInfo;
import com.cmy.service.MenuService;
import com.cmy.util.APIName;
import com.cmy.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 描述：权限菜单controller
 *
 * @author 陈淼英
 * @create 2021/3/31 0031 14:45
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @APIName("获取菜单")
    @GetMapping("/menus")
    public CommonResult getMenu(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String userId = JWTUtil.getUserId(token);
        return menuService.getMenus(userInfoMapper.selectById(userId).getRoleId());
    }

    @APIName("获取全部权限")
    @GetMapping("/allMenus")
    public CommonResult getAllMenus(HttpServletRequest request){
        return menuService.getAllMenus();
    }

    //修改角色对应的权限
    @APIName("分配角色权限")
    @PostMapping("/userRight/{roleId}")
    public CommonResult editRoleRight(@PathVariable String roleId,@RequestParam(name = "idStr") String idStr){
        return menuService.editRoleMenu(Integer.parseInt(roleId),idStr);
    }
    /**
     * 删除权限
     * @param id
     * @return
     */
    @APIName("展开点击删除权限")
    @DeleteMapping("/deleteRight/{id}/{roleId}")
    public CommonResult deleteRightById(@PathVariable Integer id,@PathVariable Integer roleId){
        return menuService.deleteRightById(id,roleId);
    }
}
