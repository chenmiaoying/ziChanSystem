package com.cmy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmy.common.response.CommonResult;
import com.cmy.dao.*;
import com.cmy.pojo.*;
import com.cmy.service.CheckService;
import com.cmy.service.DoListService;
import com.cmy.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/4/29 0029 18:53
 */
@Service
public class DoListServiceImpl implements DoListService {

    @Autowired
    private DoListMapper doListMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private AssetBuyMapper assetBuyMapper;
    @Autowired
    private ApplyRoleMapper applyRoleMapper;
    @Autowired
    private AssetRepairMapper assetRepairMapper;
    @Autowired
    private BorrowCheckMapper borrowCheckMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    private List<ToDoList> list=new ArrayList<ToDoList>();


    @Override
    public CommonResult getDolist(HttpServletRequest request, DoList doList) {
        String token = request.getHeader("Authorization");
        String userId = JWTUtil.getUserId(token);
        Integer roleId = userInfoMapper.selectById(userId).getRoleId();
        List<Integer> menuId = roleMenuMapper.getMenuId1(roleId);
        if(!menuId.isEmpty()){
            //审批借用工单
            if(menuId.contains(46)){
                List<ToDoList> toDoListB0 = borrowCheckMapper.getToDoList0(userId);
                list.addAll(toDoListB0);
            }
            //审批确认信息归还
            if(menuId.contains(47)){
                List<ToDoList> toDoListB5 = borrowCheckMapper.getToDoList5(userId);
                list.addAll(toDoListB5);
            }
            //发起资产归还
            if(menuId.contains(85)){
                List<ToDoList> toDoListB4 = borrowCheckMapper.getToDoList4(userId);
                list.addAll(toDoListB4);
            }
            //申购审批
            if (menuId.contains(73)){
                List<ToDoList> toDoListA0 = assetBuyMapper.getToDoList0(userId);
                list.addAll(toDoListA0);
            }
            //确认维修工单
            if(menuId.contains(40)){
                List<ToDoList> toDoListR1 = assetRepairMapper.getToDoList1();
                list.addAll(toDoListR1);
            }
            //验收维修工单
            if(menuId.contains(37)){
                List<ToDoList> toDoListR3 = assetRepairMapper.getToDoList3(userId);
                list.addAll(toDoListR3);
            }
            //审批账号申请
            if(menuId.contains(90)){
                List<ToDoList> toDoListU0 = userInfoMapper.getToDoList0();
                list.addAll(toDoListU0);
            }
            //审批权限申请
            if(menuId.contains(91)){
                List<ToDoList> toDoListRO3 = applyRoleMapper.getToDoList0();
                list.addAll(toDoListRO3);
            }
        }

//        QueryWrapper<DoList> myQuery = new QueryWrapper<DoList>();
//        Date[] times=doList.getTimes();
//        if(times.length==2){
//            myQuery.between("time",times[0],times[1]);
//        }
//        List<DoList> doLists = doListMapper.selectList(myQuery);
        return CommonResult.successCommonResult(list,"查询成功");
    }
//    public QueryWrapper getBorrowWrapper(String userId){
//        Integer[] arr=new Integer[]{0,5};
//        QueryWrapper<BorrowCheck> my = new QueryWrapper<BorrowCheck>();
//        my.in("status",arr).eq("check_id",userId).or()
//                .eq("status",4).eq("start_id",userId);
//        return my;
//    }
//    public QueryWrapper getBuyWrapper(String userId){
//        Integer[] arr=new Integer[]{0,5};
//        QueryWrapper<AssetBuy> my = new QueryWrapper<AssetBuy>();
//        my.eq("status",0).eq("check_id",userId);
//        return my;
//    }
//    public QueryWrapper getRepairWrapper(String userId){
//        Integer[] arr=new Integer[]{0,5};
//        QueryWrapper<AssetBuy> my = new QueryWrapper<AssetBuy>();
//        my.eq("status",3).eq("accept_id",userId);
//        return my;
//    }
}
