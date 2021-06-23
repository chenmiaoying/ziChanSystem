package com.cmy.service;

import com.cmy.common.response.CommonResult;
import com.cmy.pojo.ApplyRole;
import com.cmy.pojo.UserInfo;
import com.cmy.pojo.UserLogin;
import com.cmy.pojo.UserVo;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserInfoService {
    /**
     * 查询全部用户
     * @param limit 第几页
     * @param offset 偏移量
     * @param userInfo 查询条件
     * @return
     */
    CommonResult selectUserInfo(Integer limit, Integer offset, UserInfo userInfo);

    /**
     * 通过用户id删除用户
     * @param id 需要删除的id
     * @param limit 删除后查看第几页
     * @param offset 偏移值
     * @return
     */
    CommonResult deleteById(Integer limit,Integer offset,Integer id);


    /**
     * 通过id查找对应的用户信息
     * @param id 用户学工号
     * @return
     */
    CommonResult showById(String id);

    /**
     * 修改用户资料
     * @param userInfo
     * @return
     */
    CommonResult update(UserInfo userInfo);

    /**
     * 根据id获得该用户信息
     * @param id
     * @return
     */
    CommonResult getUserById(Integer id);

    /**
     * 添加用户
     */

    CommonResult addUser(UserInfo userInfo);

//    /**
//     * excel添加用户模板下载
//     * @param response 响应
//     */
//    void downloadExcelTemplate(HttpServletResponse response);

    /**
     * 重置用户密码
     * @param id
     * @return
     */
    CommonResult userReset(String id);

    /**
     * 得到账号申请信息
     * @return
     */
    CommonResult getAccount(Integer limit, Integer offset, UserLogin userVo);

    /**
     * 得到权限申请信息
     * @return
     */
    CommonResult getApplyRole(Integer limit, Integer offset, UserLogin userVo);

    /**
     * 账号审批
     * @param userInfo
     * @return
     */
    CommonResult sureAccount(Integer id,UserInfo userInfo);

    /**
     * 角色权限审批
     * @param id
     * @param str
     * @return
     */
    CommonResult sureApplyRole(Integer flag, Integer id,String str);

    /**
     * 角色
     * @param applyRole
     * @return
     */
    CommonResult applyRole(ApplyRole applyRole);
}
