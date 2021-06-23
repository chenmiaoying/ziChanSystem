package com.cmy.service.impl;

import com.cmy.common.response.CommonResult;
import com.cmy.dao.DefaultPasswordMapper;
import com.cmy.dao.UserInfoMapper;
import com.cmy.dao.UserMapper;
import com.cmy.pojo.DefaultPassword;
import com.cmy.pojo.Response;
import com.cmy.pojo.UserInfo;
import com.cmy.pojo.UserLogin;

import com.cmy.service.UserVoService;
import com.cmy.util.AesUtil;
import com.cmy.util.JWTUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


/**
 * @author
 */
@Service
public class UserVoServiceImpl implements UserVoService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private DefaultPasswordMapper defaultPasswordMapper;

    private PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();


    //密钥
    private String AES_KEY = "cBssbHB3ZA==HKXT";



    @Override
    public Response<String> resetPassword(String newPwd, HttpServletRequest req) {
        Response<String> responseVo = null;
        String token = req.getHeader("Authorization");
        String userId = JWTUtil.getUserId(token);
        UserLogin userLogin=userMapper.selectById(userId);
        if(newPwd != null){
            //确定与初始密码不一致
            if(userLogin.getStatus()==1) {
                //解密
                try {
                    newPwd = AesUtil.aesDecrypt(newPwd, AES_KEY);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                boolean flag = passwordEncoder.matches(userLogin.getPassword(),newPwd);

                if (flag) {
                    responseVo = new Response<>("501", null, null, "密码与原密码相同，请修改新的密码");
                } else {

                    userLogin.setStatus(0);
                    userLogin.setPassword(passwordEncoder.encode(newPwd));
                    userMapper.updateById(userLogin);
                    responseVo = new Response<>("200", null, null, "修改成功，请重新登陆");
                }
            }else {
                responseVo = new Response<>("505", null, null, "你已经修改过密码了");
            }
        }else {
            responseVo = new Response<>("504",null,null,"密码不能为空，请输入新的密码");
        }
        return responseVo;
    }

    @Override
    public CommonResult registerUser(UserInfo userInfo) {
        if(userInfo.getId()==null || userInfo.getId().equals("")){
            return CommonResult.errorCommonResult("工号不能为空");
        }
        UserInfo info = userInfoMapper.selectById(userInfo.getId());
        if(info==null){
            userInfo.setIsUser(0);
            userInfo.setApplyTime(new Date());
            userInfo.setAction1(userInfo.getUsername()+"发起账号请求，请审核");
            userInfoMapper.insert(userInfo);
            return CommonResult.successCommonResult("成功发起申请，请等待管理员验证通过");
        }
        return CommonResult.errorCommonResult("该工号已经有账号了，请核对申请账号是否正确！");
    }

    @Override
    public CommonResult editP(String id) {
        if(id!=null && !id.equals("")){
            try {
                String s = AesUtil.aesDecrypt(id, AES_KEY);
                DefaultPassword defaultPassword1 = new DefaultPassword(1,s);
                defaultPasswordMapper.updateById(defaultPassword1);
                return CommonResult.successCommonResult("修改成功");
            }catch (Exception e){
                e.printStackTrace();
                return CommonResult.successCommonResult("服务器拥挤，请稍后");
            }
        }
        return CommonResult.errorCommonResult("默认密码不能为空");
    }
}
