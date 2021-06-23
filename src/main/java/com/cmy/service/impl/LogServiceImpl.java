package com.cmy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmy.common.response.CommonResult;
import com.cmy.dao.*;
import com.cmy.pojo.*;
import com.cmy.service.LogService;
import com.cmy.util.APIName;
import com.cmy.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class LogServiceImpl implements LogService, CommandLineRunner {

    @Autowired
    private WebApplicationContext applicationContext;
    @Autowired
    private AllApiMapper allApiMapper;
    @Autowired
    private UserBehaviorMapper userBehaviorMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private BorrowCheckMapper checkMapper;
    @Autowired
    private AssetQuery assetQuery;
    @Autowired
    private ApplyRoleMapper applyRoleMapper;
    @Autowired
    private BorrowCheckMapper borrowCheckMapper;

    @Autowired
    private SysLogMapper sysLogMapper;
    @Autowired
    private DoListMapper doListMapper;


    @Override
    public void run(String... args) throws Exception {
        init();
    }

    public void init() {
        setAllApi();
    }

    void setAllApi() {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        AllApi allApi = new AllApi();
        AllApi allApiDB = null;
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {

            RequestMappingInfo info = m.getKey();
            HandlerMethod method = m.getValue();
            PatternsRequestCondition p = info.getPatternsCondition();//得到请求路径
            for (String url : p.getPatterns()) {
                allApi.setApiUrl(url);
            }
            allApi.setApiClassName(method.getMethod().getDeclaringClass().getName());
            allApi.setApiMethodName(method.getMethod().getName());
            RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
//            allApi.setApiActionName(method.getMethodAnnotation(APIName.class).value());
            for (RequestMethod requestMethod : methodsCondition.getMethods()) {
                allApi.setApiType(requestMethod.toString());
            }
            allApiDB = allApiMapper.selectOne(new QueryWrapper<AllApi>().eq("api_url", allApi.getApiUrl()).eq("api_type",allApi.getApiType()));
            if(allApiDB == null){
//                allApi.setApiActionName(method.getMethodAnnotation(APIName.class).value());
                allApiMapper.insert(allApi);
            } else {
                allApi.setId(allApiDB.getId());
                allApiMapper.updateById(allApi);
            }
        }
    }
    
    @Override
    public UserBehavior getUserBehaviorConfigByUrlAndType(String url, String type) {
        return userBehaviorMapper.selectOne(new QueryWrapper<UserBehavior>().eq("url", url).eq("type", type));
    }

    @Override
    public CommonResult getUserLog(SysLog sysLog,HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String userId = JWTUtil.getUserId(token);
        UserInfo info = userInfoMapper.selectById(userId);
        if(info.getRoleId()==1){
            QueryWrapper<SysLog> queryWrapper = new QueryWrapper<>();
            Date[] times = sysLog.getTimes();
            if(times!=null && times.length==2){
                queryWrapper.between("create_time",times[0],times[1]);
            }
            List<SysLog> sysLogs = sysLogMapper.selectList(queryWrapper);

            return CommonResult.successCommonResult(sysLogs,"查询成功");
        }else {
            QueryWrapper<DoList> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("userId",userId);
            Date[] times = sysLog.getTimes();
            if(times!=null && times.length==2){
                queryWrapper1.between("time",times[0],times[1]);
            }
            List<DoList> lists=doListMapper.selectList(queryWrapper1);
            return CommonResult.successCommonResult(lists,"查询成功");
        }
    }

    @Override
    public CommonResult getSum(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String userId = JWTUtil.getUserId(token);
        UserInfo info = userInfoMapper.selectById(userId);
        Integer flag=2;
        if(info.getRoleId()==1){
            flag=1;
            Integer i = doListMapper.selectCount(new QueryWrapper<DoList>().eq("userId", userId));
            Integer j=userInfoMapper.selectCount(null);
            Integer z = assetQuery.selectCount(null);
            return CommonResult.successCommonResult(new ReturnShou(i,j,z,flag),"查询成功");
        }
        Integer i = doListMapper.selectCount(new QueryWrapper<DoList>().eq("userId", userId));
        Integer j = assetQuery.selectCount(new QueryWrapper<Asset>().eq("host_id",info.getId()));
        Integer [] ints=new Integer[2];
        ints[0]=3;
        ints[1]=4;
        Integer z = checkMapper.selectCount(new QueryWrapper<BorrowCheck>()
                .eq("start_people",info.getUsername()).in("status",ints));
        return CommonResult.successCommonResult(new ReturnShou(i,j,z,flag),"查询成功");
    }

    @Override
    public CommonResult getList(Integer limit, Integer offset, SysLog sysLog) {
        QueryWrapper<SysLog> wrapper = getWrapper(sysLog);
        Page<SysLog> sysLogPage = sysLogMapper.selectPage(new Page<>(limit,offset), wrapper);
        return CommonResult.successCommonResult(sysLogPage,"查询成功");
    }

    @Override
    public CommonResult deleteQueryAll(SysLog sysLog) {
        QueryWrapper<SysLog> wrapper = getWrapper(sysLog);
        sysLogMapper.delete(wrapper);
        return CommonResult.successCommonResult("清空完毕");
    }
    public QueryWrapper<SysLog> getWrapper( SysLog sysLog){
        QueryWrapper<SysLog> queryWrapper = new QueryWrapper<>();
        if(sysLog.getUsername()!=null && !sysLog.getUsername().equals("")){
            queryWrapper.like("username",sysLog.getUsername());
        }
        if(sysLog.getOperation()!=null && !sysLog.getOperation().equals("")){
            queryWrapper.like("operation",sysLog.getOperation());
        }
        Date[] times = sysLog.getTimes();
        if(times!=null && times.length==2){
            queryWrapper.between("create_time",times[0],times[1]);
        }
        return queryWrapper;
    }

    @Override
    public CommonResult deleteByStr(String sysLogs) {
        String[] split = sysLogs.split(",");
        sysLogMapper.delete(new QueryWrapper<SysLog>().in("id",split));
        return CommonResult.successCommonResult("删除成功");
    }

    @Override
    public CommonResult getBehaviorApi(Integer limit, Integer offset, UserBehavior behavior) {
        QueryWrapper<UserBehavior> queryWrapper = new QueryWrapper<>();
        if(behavior.getApiDescribe()!=null && !behavior.getApiDescribe().equals("")){
            queryWrapper.like("api_describe",behavior.getApiDescribe());
        }
        Page<UserBehavior> page = userBehaviorMapper.selectPage(new Page<>(limit, offset), queryWrapper);
        return CommonResult.successCommonResult(page,"查询成功");
    }

    @Override
    public CommonResult deleteApiManage(String idStr) {
        String[] split = idStr.split(",");
        userBehaviorMapper.delete(new QueryWrapper<UserBehavior>().in("id",split));
        return CommonResult.successCommonResult("删除成功");
    }

    @Override
    public CommonResult getAllApi() {
        List<AllApi> allApis = allApiMapper.selectList(new QueryWrapper<AllApi>().eq("status",0));
        return CommonResult.successCommonResult(allApis,"查询成功");
    }

    @Override
    public CommonResult addBehavior(String str) {
        String[] split = str.split(",");
        List<AllApi> addApi = allApiMapper.selectList(new QueryWrapper<AllApi>().in("id", split));
        for (AllApi a:addApi) {
            UserBehavior userBehavior=new UserBehavior();
            userBehavior.setCreateTime(new Date());
            userBehavior.setApiDescribe(a.getApiActionName());
            userBehavior.setUrl(a.getApiUrl());
            userBehavior.setType(a.getApiType());
            userBehaviorMapper.insert(userBehavior);
            a.setStatus(1);
            allApiMapper.updateById(a);
        }
        return CommonResult.successCommonResult("添加成功");
    }

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void addDoList(){
        //权限申请
        List<ApplyRole> applyRoles = applyRoleMapper.selectList(new QueryWrapper<ApplyRole>().eq("status", 1));
        for (ApplyRole a:applyRoles) {
            DoList doList = new DoList();
            doList.setUserId(a.getUserId());
            doList.setCreateTime(a.getCheckTime());
            doList.setOperation(a.getAction1());
            doListMapper.insert(doList);
        }
        List<BorrowCheck> status = borrowCheckMapper.selectList(new QueryWrapper<BorrowCheck>().eq("status", 2));
        for (BorrowCheck b:status) {
            DoList doList = new DoList();
            doList.setUserId(b.getStartId());
            doList.setOperation("借用的"+b.getAssetName()+"审核通过待领用");
        }
    }
}
