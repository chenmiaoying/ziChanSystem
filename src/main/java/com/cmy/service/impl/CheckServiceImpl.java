package com.cmy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmy.common.response.CommonResult;
import com.cmy.dao.*;
import com.cmy.pojo.*;
import com.cmy.service.CheckService;
import com.cmy.util.ExportWordUtils;
import com.cmy.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 描述：
 *
 * @author 陈淼英
 * @create 2021/4/9 0009 17:11
 */
@Service
public class CheckServiceImpl implements CheckService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private AssetQuery assetQuery;

    @Autowired
    private BorrowCheckMapper borrowCheckMapper;
    @Autowired
    private DoListMapper doListMapper;

    @Override
    public CommonResult addBCheck(BorrowCheck borrowCheck, HttpServletRequest request) {
        if(borrowCheck.getAssetName()==null || borrowCheck.getAssetName().equals("")){
            return CommonResult.errorCommonResult("资产名称不能为空");
        }
        //判断是否已经发过同资产的借用申请但是还没有关闭
        BorrowCheck borrowCheck1 = borrowCheckMapper.selectOne(new QueryWrapper<BorrowCheck>().eq("asset_name", borrowCheck.getAssetName())
                .eq("workStatus", 1));
        if(borrowCheck1!=null){
            return CommonResult.errorCommonResult("你之前发起该资产的工单还没有关闭，请先关闭再重新发起");
        }else{
            String username=getUserName(request);
            borrowCheck.setStartPeople(username);

            List<Asset> asset_name = assetQuery.selectList(new QueryWrapper<Asset>().eq("asset_name", borrowCheck.getAssetName()));
            if (asset_name.isEmpty()){
                return CommonResult.errorCommonResult("借用资产不存在，请重新核对");
            }
            Asset asset=asset_name.get(0);
            borrowCheck.setCheckId(asset.getHostId());
            borrowCheck.setCheckPeople(asset.getAssetHost());

            Date date=new Date();
            borrowCheck.setApplyTime(date);
            borrowCheck.setStatus(0);
            borrowCheck.setAction1(username+"新增借用工单待审批");
            borrowCheckMapper.insert(borrowCheck);

            return CommonResult.successCommonResult("发起成功");
        }
    }

    @Override
    public CommonResult getChecks(Integer limit ,Integer offset ,HttpServletRequest request,BorrowCheck borrowCheck) {
        if(borrowCheck.getAssetName()==""){
            borrowCheck.setAssetName(null);
        }
        if(borrowCheck.getCheckPeople()==""){
            borrowCheck.setCheckPeople(null);
        }
        String username=getUserName(request);
        borrowCheck.setStartPeople(username);
        Page<BorrowCheck> pages = borrowCheckMapper.selectPage(new Page<>(limit, offset), new QueryWrapper<>(borrowCheck));

        return CommonResult.successCommonResult(pages,"查询成功");
    }

    @Override
    public CommonResult closeCheck(Integer checkId, HttpServletRequest request) {
        String username=getUserName(request);
        BorrowCheck borrowCheck = borrowCheckMapper.selectById(checkId);

        //
        if(!borrowCheck.getStartPeople().equals(username)){
            return CommonResult.errorCommonResult("你不是该工单发起人，你不可以关闭该工单");
        }else if(borrowCheck.getStatus()==3||borrowCheck.getStatus()==4){
            //没有归还不可以关闭该工单
            return CommonResult.errorCommonResult("你还没有归还该资产");
        }else {
            borrowCheck.setWorkStatus(0);
            borrowCheckMapper.update(borrowCheck,new QueryWrapper<BorrowCheck>().eq("borrow_check_id",checkId));
            return CommonResult.successCommonResult("关闭成功");
        }
    }

    @Override
    public CommonResult getBorrowChecks(Integer flag,Integer limit, Integer offset, HttpServletRequest request, BorrowCheck borrowCheck) {
        //得到该用户的username,相应得到审核人为该username的借用工单
        String username=getUserName(request);
        if(flag==0){
            //未审核
            if(borrowCheck.getStatus()==null){
                QueryWrapper<BorrowCheck> myWrapper = new QueryWrapper<>();
                myWrapper.eq("check_people",username);
                if(borrowCheck.getAssetName()!=null)
                {
                    myWrapper.like("asset_name",borrowCheck.getAssetName());
                }
                if(borrowCheck.getStartPeople()!=null){
                    myWrapper.like("start_people",borrowCheck.getStartPeople());
                }
                myWrapper.and(wrapper->wrapper.eq("status",0).or().eq("moreStatus",1));
                Page<BorrowCheck> pages = borrowCheckMapper.selectPage(new Page<>(limit, offset), myWrapper);
                return CommonResult.successCommonResult(pages,"查询成功");
            }else if(borrowCheck.getStatus()==0){
                Page<BorrowCheck> pages = borrowCheckMapper.selectPage(new Page<>(limit, offset), new QueryWrapper<>(borrowCheck));
                return CommonResult.successCommonResult(pages,"查询成功");
            }else {
                return CommonResult.successCommonResult("借用待审核页面只有借用待审核工单");
            }
        }else if(flag==2){
            //归还确认
            if(borrowCheck.getStatus()!=null && borrowCheck.getStatus()!=5){
                return CommonResult.successCommonResult("归还待审核页面只有归还待审核工单");
            }
            borrowCheck.setStatus(5);
            Page<BorrowCheck> pages = borrowCheckMapper.selectPage(new Page<>(limit, offset), new QueryWrapper<BorrowCheck>(borrowCheck));
            return CommonResult.successCommonResult(pages,"查询成功");
        }else if(flag==3){
            //全部工单
            Page<BorrowCheck> pages = borrowCheckMapper.selectPage(new Page<>(limit, offset), new QueryWrapper<>(borrowCheck));
            return CommonResult.successCommonResult(pages,"查询成功");
        }
        return CommonResult.errorCommonResult("查询失败，非法访问");
    }

    @Override
    public CommonResult allowCheck(Integer flag, Integer id, HttpServletRequest request,BorrowCheck b) {
        //1、判断是否是资产权限用户
        String username=getUserName(request);
        BorrowCheck borrowCheck = borrowCheckMapper.selectById(id);
        if(!borrowCheck.getCheckPeople().equals(username)){
            return CommonResult.errorCommonResult("非法访问，你不是合法审核人");
        }
        if(flag==1){
            if(borrowCheck.getMoreStatus()!=null && borrowCheck.getMoreStatus()==1){
                borrowCheck.setExpReturnTime(borrowCheck.getAfMoreTime());
                borrowCheck.setStatus(3);
                borrowCheck.setMoreStatus(0);
                borrowCheck.setAction1("工单已审批");
                borrowCheckMapper.updateById(borrowCheck);
                return CommonResult.successCommonResult("操作成功");
            }
            //同意
            //判断该资产是否已经被归还
            String assetName = borrowCheck.getAssetName();
            Asset asset = assetQuery.selectOne(new QueryWrapper<Asset>().eq("asset_name", assetName));
            if(asset.getAssetBack()==1) {
//                判断该资产是否还存在
                if(asset.getAssetDelete()==1){
                    return CommonResult.errorCommonResult("该资产已经报废被删除了");
                }else {

                    borrowCheck.setCheckTime(new Date());
                    borrowCheck.setStatus(2);
                    borrowCheckMapper.updateById(borrowCheck);
                    return CommonResult.successCommonResult("操作成功");
                }
            }else {
                return CommonResult.errorCommonResult("该资产还没有被归还，不可以借出");
            }
        }else if(flag==0){
            if(borrowCheck.getMoreStatus()==1){
                borrowCheck.setMoreStatus(0);
                borrowCheck.setRefuseReason(b.getRefuseReason());
                borrowCheck.setAfMoreTime(null);
                borrowCheckMapper.updateById(borrowCheck);
//                doListMapper.insert(getDoList("资产待领用",getPeopleId(borrowCheck.getStartPeople())));
                return CommonResult.successCommonResult("操作成功");

            }
            //拒绝
            borrowCheck.setRefuseReason(b.getRefuseReason());
            borrowCheck.setStatus(1);
            borrowCheckMapper.updateById(borrowCheck);
            return CommonResult.successCommonResult("操作成功");
        }
        return CommonResult.errorCommonResult("非法访问");
    }

    @Override
    public CommonResult useAsset(Integer id) {

        BorrowCheck borrowCheck = borrowCheckMapper.selectById(id);
        String assetName = borrowCheck.getAssetName();
        Asset asset = assetQuery.selectOne(new QueryWrapper<Asset>().eq("asset_name", assetName));
        //修改该资产的基本信息
        asset.setAssetBack(0);
        asset.setAssetCondition("使用中");
        asset.setAssetUser(borrowCheck.getStartPeople());
        assetQuery.updateById(asset);

        borrowCheck.setStatus(3);
        borrowCheck.setUseTime(new Date());
        borrowCheck.setAction1(borrowCheck.getStartPeople()+"已经领用了资产");
        borrowCheckMapper.updateById(borrowCheck);

        return CommonResult.successCommonResult("领用成功,可自行下载领用单");
    }

    @Override
    public CommonResult backAsset(Integer id) {
        BorrowCheck borrowCheck = borrowCheckMapper.selectById(id);
        borrowCheck.setStatus(5);
        borrowCheck.setBackTime(new Date());
        borrowCheck.setAction1("资产待确认归还");
        borrowCheck.setAction1(borrowCheck.getStartPeople()+"借用的"+borrowCheck.getAssetName()+"待确认归还");
        borrowCheckMapper.updateById(borrowCheck);
//        doListMapper.insert(getDoList("资产待确认归还",getPeopleId(borrowCheck.getCheckPeople())));
        return CommonResult.successCommonResult("成功发起归还，待审核人确认归还");
    }

    @Override
    public CommonResult isBackAsset(Integer id,HttpServletRequest request) {
        String username=getUserName(request);
        BorrowCheck borrowCheck = borrowCheckMapper.selectById(id);

        if(!borrowCheck.getCheckPeople().equals(username)){
            return CommonResult.errorCommonResult("非法访问，你不是合法审核人");
        }

        String assetName = borrowCheck.getAssetName();
        Asset asset = assetQuery.selectOne(new QueryWrapper<Asset>().eq("asset_name", assetName));
        asset.setUserId(borrowCheck.getCheckId());
        asset.setAssetBack(1);
        asset.setAssetCondition("闲置中");
        asset.setAssetUser(username);
        assetQuery.updateById(asset);
        asset.setAssetUser(username);
        borrowCheck.setStatus(6);
        borrowCheckMapper.updateById(borrowCheck);
//        doListMapper.insert(getDoList(borrowCheck.getAssetName()+"已经确认归还，待关闭工单",getPeopleId(borrowCheck.getStartPeople())));
        return CommonResult.successCommonResult("确认归还");
    }

    public String getUserName(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String userId = JWTUtil.getUserId(token);
        String username = userMapper.selectById(userId).getUsername();

        return username;
    }

    @Override
    public CommonResult moreTime(BorrowCheck borrowCheck) {
        BorrowCheck borrowCheck1 = borrowCheckMapper.selectById(borrowCheck.getBcId());
        borrowCheck1.setMoreStatus(1);
        borrowCheck1.setMoreReason(borrowCheck.getMoreReason());
        borrowCheck1.setAfMoreTime(borrowCheck.getAfMoreTime());
        borrowCheck.setAction1(borrowCheck1.getStartPeople()+"发起借用延期申请待审核");
        borrowCheckMapper.updateById(borrowCheck1);
//        doListMapper.insert(getDoList("借用延期申请待审核",getPeopleId(borrowCheck1.getCheckPeople())));
        return CommonResult.successCommonResult("延期申请发起成功");
    }




    public String getPeopleId(String username){
        String userId=userInfoMapper.selectOne(new QueryWrapper<UserInfo>().eq("username",username)).getId();
        return userId;
    }

    /**
     * 借用申请超时未还,12点和23点执行一次
     */
    @Scheduled(cron = "0 0 12,23 * * ?")
    public void assetIsOrBack(){

        List<BorrowCheck> borrowChecks = borrowCheckMapper.selectList(new QueryWrapper<BorrowCheck>().eq("status",3));
        for (BorrowCheck c:borrowChecks) {
            Date expReturnTime = c.getExpReturnTime();//预计归还时间
            Date date = new Date();//现在的时间
            int result=date.compareTo(expReturnTime);

            if(result>=0){
                c.setStatus(4);
                c.setAction1("借用的资产"+c.getAssetName()+"待归还");
                borrowCheckMapper.updateById(c);
//                doListMapper.insert(getDoList("资产待归还",getPeopleId(c.getStartPeople())));
            }
        }

    }

    @Override
    public CommonResult downUse(HttpServletRequest request, HttpServletResponse response, Integer id) {
        String token = request.getHeader("Authorization");
        String userId = JWTUtil.getUserId(token);
        BorrowCheck borrowCheck = borrowCheckMapper.selectById(id);
        if(borrowCheck.getStartId()!=null&&!borrowCheck.getStartId().equals(userId)){
            return CommonResult.errorCommonResult("你不是借用工单发起人，不可以下载领用单");
        }
        if(borrowCheck.getStatus() == 1){
            return CommonResult.errorCommonResult("审核不通过，不可以下载领用单");
        }else if(borrowCheck.getStatus() == 0){
            return CommonResult.errorCommonResult("还未审核，不可以下载领用单");
        }else {
            Asset asset_name = assetQuery.selectOne(new QueryWrapper<Asset>().eq("asset_name", borrowCheck.getAssetName()));
            Map<String, Object> params=getMap(borrowCheck,asset_name);
            ClassPathResource resource = new ClassPathResource("static/res/excel/资产领用单.docx");
            ClassPathResource resource2 = new ClassPathResource("static/res/excel/");
            String templatePath="/home/cmy/data/资产领用单.docx";
            String temDir="/home/cmy/data";
//            String templatePath= File.separator + "static" + File.separator
//                    + "res" + File.separator + "excel" + File.separator + "资产领用单.docx";
//            String temDir=resource2.getPath();
//                    File.separator + "static" + File.separator
//                    + "res" + File.separator + "excel"+ File.separator;
            String fileName = "assetUse.docx";
            ExportWordUtils.exportWord(templatePath,temDir,fileName,params,request,response);
            return null;
        }
    }
    public Map<String,Object> getMap(BorrowCheck borrowCheck,Asset asset){
        SimpleDateFormat fo = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("applyUsername",borrowCheck.getStartPeople());
        map.put("apply",borrowCheck.getStartId());
        map.put("check",borrowCheck.getCheckPeople());
        map.put("checkId",borrowCheck.getCheckId());
        map.put("applyTime",fo.format(borrowCheck.getApplyTime()));
        map.put("checkTime",fo.format(borrowCheck.getCheckTime()));
        map.put("expReturnTime",fo.format(borrowCheck.getExpReturnTime()));
        map.put("name",asset.getAssetName());
        map.put("asset",String.valueOf(asset.getAssetId()));
        map.put("point",asset.getPointAllName());
        map.put("address",asset.getAddress());
        map.put("type",asset.getAssetType());
        map.put("use",fo.format(borrowCheck.getUseTime()));

        return map;
    }
}
