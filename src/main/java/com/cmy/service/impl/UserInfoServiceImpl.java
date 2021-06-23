package com.cmy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmy.common.response.CommonResult;
import com.cmy.dao.ApplyRoleMapper;
import com.cmy.dao.DefaultPasswordMapper;
import com.cmy.dao.UserInfoMapper;
import com.cmy.dao.UserMapper;
import com.cmy.pojo.*;
import com.cmy.service.MenuService;
import com.cmy.service.UserInfoService;
import com.cmy.util.JWTUtil;
import com.cmy.util.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

/**
 * @author Tim
 * @date 2021/3/23
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserMapper userMapper;//用户登录表
    @Autowired
    private MenuService menuService;
    @Autowired
    private ApplyRoleMapper applyRoleMapper;
    @Autowired
    private DefaultPasswordMapper defaultPasswordMapper;

    private PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    @Override
    public CommonResult selectUserInfo(Integer limit, Integer offset, UserInfo userInfo) {
        QueryWrapper<UserInfo> my = new QueryWrapper<>();
        if(userInfo.getId()!=null && !userInfo.getId().equals("")){
            my.like("id",userInfo.getId());
        }
        my.eq("is_user",1);
        if(userInfo.getRoleId()!=null){
            my.eq("role_id",userInfo.getRoleId());
        }
        Page<UserInfo> userInfoPage = userInfoMapper.selectPage(new Page<>(limit, offset), my);
        return CommonResult.successCommonResult(userInfoPage,"查询成功");
    }

    @Override
    public CommonResult deleteById(Integer limit,Integer offset,Integer id) {
        userInfoMapper.deleteById(id);

        Page<UserInfo> userInfoPage = userInfoMapper.selectPage(new Page<>(limit, offset), null);
        return CommonResult.successCommonResult(userInfoPage,"删除成功");
    }

    @Override
    public CommonResult showById(String token) {
        String idstr=(JWTUtil.getUserId(token));
        Integer id=Integer.parseInt(idstr);
        UserInfo userInfo = userInfoMapper.selectById(id);
        return CommonResult.successCommonResult(userInfo,"查询成功");
    }

    @Override
    public CommonResult update(UserInfo userInfo) {
        int i = userInfoMapper.updateById(userInfo);
        if(i==1){
            return CommonResult.successCommonResult("修改成功");
        }
        else {
            return CommonResult.errorCommonResult("修改失败");
        }
    }

    @Override
    public CommonResult getUserById(Integer id) {
        UserInfo info = userInfoMapper.selectById(id);
        return CommonResult.successCommonResult(info,"查询成功");
    }

    @Override
    public CommonResult addUser(UserInfo userInfo) {
        if(userInfo.getId()==null || userInfo.getId().equals("") || userInfo.getRoleId()==null || userInfo.getUsername()==null){
            return CommonResult.errorCommonResult("工号、用户名和对应的角色都不能为空");
        }else{
            UserLogin userLogin1 = userMapper.selectById(userInfo.getId());
            if(userLogin1 == null){
                UserLogin userLogin = new UserLogin();

                userInfo.setIsUser(1);
                userInfo.setCheckTime(new Date());
                userInfo.setAction1("新增一个用户");
                userInfoMapper.insert(userInfo);

                //将该用户添加到用户登录表中，设置默认密码123456
                userLogin.setId(userInfo.getId());
                userLogin.setUsername(userInfo.getUsername());

                userLogin.setPassword(passwordEncoder.encode(defaultPasswordMapper.selectById(1).getPassword()));
                userLogin.setStatus(1);
                userMapper.insert(userLogin);
                return CommonResult.successCommonResult("添加成功");
            }else{
                return CommonResult.errorCommonResult("该用户已存在，请修改用户名");
            }

        }
    }


    @Override
    public CommonResult userReset(String id) {
        UserLogin userLogin = userMapper.selectById(id);
        if(userLogin==null){
            return CommonResult.errorCommonResult("该用户不存在");
        }else{
            userLogin.setPassword(passwordEncoder.encode(defaultPasswordMapper.selectById(1).getPassword()));
            userLogin.setStatus(1);
            userMapper.updateById(userLogin);
            return CommonResult.successCommonResult("重置密码成功");
        }
    }

    @Override
    public CommonResult getAccount(Integer limit, Integer offset, UserLogin userVo) {

        QueryWrapper<UserInfo> myWrapper = new QueryWrapper<>();
        myWrapper.eq("is_user",0);
        if(!userVo.getUsername().equals("")&& userVo.getUsername()!=null)
        {
            myWrapper.like("username",userVo.getUsername());
        }
        if(!userVo.getId().equals("")&& userVo.getUsername()!=null){
            myWrapper.like("id",userVo.getId());
        }
        Page<UserInfo> userInfoPage = userInfoMapper.selectPage(new Page<>(limit, offset), myWrapper);
        return CommonResult.successCommonResult(userInfoPage,"查询成功");
    }

    @Override
    public CommonResult getApplyRole(Integer limit, Integer offset, UserLogin userVo) {
        QueryWrapper<ApplyRole> myWrapper = new QueryWrapper<>();
        myWrapper.eq("status",0);
        if(!userVo.getUsername().equals("") && userVo.getUsername()!=null)
        {
            myWrapper.like("username",userVo.getUsername());
        }
        if(!userVo.getId().equals("") && userVo.getUsername()!=null){
            myWrapper.like("userId",userVo.getId());
        }
        Page<ApplyRole> applyRolePage= applyRoleMapper.selectPage(new Page<>(limit, offset), myWrapper);
        return CommonResult.successCommonResult(applyRolePage,"查询成功");
    }

    @Override
    public CommonResult sureAccount(Integer id, UserInfo userInfo) {
        if(id==1){
            //判断该工号是否已经有账号了
            UserLogin userLogin = userMapper.selectById(userInfo.getId());
            if(userLogin!=null){
                return CommonResult.errorCommonResult("该工号已经存在账号了");
            }
            UserInfo userInfo1 = userInfoMapper.selectById(userInfo.getId());
            userInfo1.setIsUser(1);
            userInfo1.setRoleId(userInfo.getRoleId());
            userInfo1.setPoint(userInfo.getPoint());
            userInfo1.setCheckTime(new Date());
            userInfoMapper.updateById(userInfo1);
            UserLogin newLogin=new UserLogin();
            newLogin.setId(userInfo.getId());
            newLogin.setUsername(userInfo1.getUsername());

            newLogin.setPassword(passwordEncoder.encode(defaultPasswordMapper.selectById(1).getPassword()));
            newLogin.setStatus(1);
            userMapper.insert(newLogin);
            return CommonResult.successCommonResult("添加成功,可通知该用户用默认密码进行登录");

        }else {
            userInfoMapper.deleteById(userInfo.getId());
            return CommonResult.errorCommonResult("操作成功");

        }
    }

    @Override
    public CommonResult sureApplyRole(Integer flag, Integer id,String str) {

        if(flag==0){
            applyRoleMapper.deleteById(id);
            return CommonResult.successCommonResult("操作成功");
        }else {
            if(str!=null && !str.equals("")){
                ApplyRole applyRole = applyRoleMapper.selectById(id);
                applyRole.setCheckTime(new Date());
                applyRole.setStatus(1);
                applyRoleMapper.updateById(applyRole);
                UserInfo userInfo = userInfoMapper.selectById(applyRole.getUserId());
                return menuService.editRoleMenu(userInfo.getRoleId(),str);
            }
            return CommonResult.errorCommonResult("分配权限不能为空");
        }
    }

    @Override
    public CommonResult applyRole(ApplyRole applyRole) {
        applyRole.setApplyTime(new Date());
        applyRole.setStatus(0);
        applyRole.setApplyTime(new Date());
        applyRole.setAction1(applyRole.getUserId()+"发起"+applyRole.getApplyRole()+"申请");
        applyRoleMapper.insert(applyRole);
        return CommonResult.successCommonResult("成功发起申请，等待管理员审批");
    }
}
